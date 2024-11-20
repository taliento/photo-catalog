package com.taliento.catalog.ui.catalog.presentation

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taliento.catalog.model.Catalog
import com.taliento.catalog.ui.catalog.domain.useCases.AddPhoto
import com.taliento.catalog.ui.catalog.domain.useCases.DeletePhoto
import com.taliento.catalog.ui.catalog.domain.useCases.GetCatalogPhotos
import com.taliento.catalog.ui.catalog.domain.useCases.GetCatalogPhotosToUpload
import com.taliento.catalog.ui.catalog.domain.useCases.GetPhoto
import com.taliento.catalog.ui.catalog.domain.useCases.UpdatePhoto
import com.taliento.catalog.ui.catalog.domain.useCases.UploadPhoto
import com.taliento.catalog.ui.catalog.presentation.CatalogScreenUiState.Success
import com.taliento.catalog.ui.catalog.presentation.EditScreenUiState.EditSuccess
import com.taliento.catalog.utils.compressImageBytes
import com.taliento.catalog.utils.getFileName
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Davide Taliento on 14/11/24.
 */
@HiltViewModel
class CatalogScreenViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    getCatalogPhotosUseCase: GetCatalogPhotos,
    private val getCatalogPhotosToUploadUseCase: GetCatalogPhotosToUpload,
    private val addPhotoUseCase: AddPhoto,
    private val deletePhotoUseCase: DeletePhoto,
    private val getPhotoUseCase: GetPhoto,
    private val updatePhotoUseCase: UpdatePhoto,
    private val uploadPhotoUseCase: UploadPhoto
) : ViewModel() {

    val uiState: StateFlow<CatalogScreenUiState> =
        getCatalogPhotosUseCase().map<List<Catalog>, CatalogScreenUiState>(::Success)
            .catch { emit(CatalogScreenUiState.Error(it)) }.stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5000), CatalogScreenUiState.Loading
            )

    fun addPhoto(path: String) {
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Log.e("AddPhoto", throwable.message.toString())
        }
        viewModelScope.launch(IO + coroutineExceptionHandler) {
            addPhotoUseCase(path)
        }
    }

    fun deletePhoto(photo: Catalog) {
        viewModelScope.launch {
            deletePhotoUseCase(photo)
        }
    }

    fun updatePhoto(photo: Catalog) {
        viewModelScope.launch {
            updatePhotoUseCase(photo)
        }
    }

    //second state the text typed by the user
    private val _uid = MutableStateFlow(0)
    val uid = _uid.asStateFlow()

    fun setUid(uid: Int) {
        _uid.value = uid
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val getByUidState: StateFlow<EditScreenUiState> =
        uid.flatMapLatest { value ->
            getPhotoUseCase(value).map<Catalog?, EditScreenUiState>(::EditSuccess)
                .catch { emit(EditScreenUiState.Error(it)) }
        }.stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), EditScreenUiState.Loading
        )

    private var uploadPhotoJob: Job? = null

    private val _uploadProgress = MutableStateFlow(0f)
    val uploadProgress = _uploadProgress.asStateFlow()

    private var _uploadFileWithProgress = MutableStateFlow<UploadUIState>(UploadUIState.NotLoading)
    val uploadFileWithProgress = _uploadFileWithProgress.asStateFlow()

    /**
     * Carica tutte le foto su catbox dopo averle compresse.
     * Le photo vengono caricate sfruttando la concurrency su thread separati.
     * Durante l'upload l'utente viene informato dello stato e inoltre è possibile annullare il processo
     *
     */
    suspend fun uploadPhotos() {
        uploadPhotoJob?.cancel() // Cancel any ongoing upload
        uploadPhotoJob = viewModelScope.launch {
            _uploadFileWithProgress.value = UploadUIState.Loading
            var uploaded = 0
            getCatalogPhotosToUploadUseCase().collect { photos ->
                val toUpload = photos.size

                val uploads = photos.map { photo ->
                    async {
                        val uri = Uri.parse(photo.path)
                        Pair(
                            photo, uploadPhotoUseCase(
                                context.compressImageBytes(uri), context.getFileName(uri)
                            )
                        )
                    }
                }

                //in questa maniera vengono eseguite in concorrenza ma posso aggiornare lo stato del singolo thread
                uploads.map {
                    try {
                        val result = it.await()
                        val url: String = result.second
                        uploaded++
                        _uploadProgress.value = uploaded.toFloat() / toUpload
                        if (url.isNotEmpty()) {
                            result.first.url = url
                        } else {
                            Log.d("UploadPhoto", "Upload problem!")
                        }
                    } catch (exception: Exception) {
                        Log.e("UploadPhoto", exception.message.toString())
                    }

                }

                if (uploaded == toUpload) {
                    _uploadFileWithProgress.value = UploadUIState.Success
                    _uploadProgress.value = 0f
                    uploadPhotoJob?.cancel()

                    viewModelScope.launch {
                        photos.forEach { photo ->
                            updatePhotoUseCase(photo)
                        }
                    }
                }
            }
        }
    }

    fun cancelUpload() {
        uploadPhotoJob?.cancel()
        _uploadFileWithProgress.value = UploadUIState.NotLoading
    }
}

