package com.taliento.catalog.ui.catalog.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.taliento.catalog.BuildConfig
import com.taliento.catalog.R
import com.taliento.catalog.model.Catalog
import com.taliento.catalog.ui.common.DialogBox
import com.taliento.catalog.ui.theme.MyApplicationTheme
import com.taliento.catalog.utils.createImageFile
import com.taliento.catalog.utils.findActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Objects


/**
 * Created by Davide Taliento on 14/11/24.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreenGrid(
    modifier: Modifier = Modifier,
    viewModel: CatalogScreenViewModel = hiltViewModel(),
    openPhoto: (Catalog) -> Unit
) {
    val context = LocalContext.current
    val catalogState by viewModel.uiState.collectAsStateWithLifecycle()
    val uploadProgress by viewModel.uploadProgress.collectAsStateWithLifecycle()
    val uploadFileWithProgress by viewModel.uploadFileWithProgress.collectAsStateWithLifecycle()
    val viewTypeGrid = remember {
        mutableStateOf(true)
    }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val file by remember {
        mutableStateOf(context.createImageFile())
    }
    val uri by remember {
        mutableStateOf(
            FileProvider.getUriForFile(
                Objects.requireNonNull(context), BuildConfig.APPLICATION_ID + ".provider", file
            )
        )
    }
    var showConfirmDialog by remember {
        mutableStateOf(false)
    }

    // Registers a photo picker activity launcher in multi-select mode.
    val pickMultipleMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (uris.isNotEmpty()) {
                Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
                uris.forEach { uri ->

                    context.grantUriPermission(
                        context.packageName,
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                    )
                    context.contentResolver.takePersistableUriPermission(
                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )

                    viewModel.addPhoto(uri.toString())

                }

            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            if (result) {
                uri?.let {
                    viewModel.addPhoto(it.toString())
                }
            }
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission(), onResult = {
            if (it) {
                Toast.makeText(
                    context, context.getString(R.string.permesso_garantito), Toast.LENGTH_SHORT
                ).show()

                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(
                    context, context.getString(R.string.permesso_revocato), Toast.LENGTH_SHORT
                ).show()
            }
        })

    Scaffold(
        topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), title = {
                Text(stringResource(id = R.string.app_name))
            }, actions = {
                IconButton(onClick = {
                    scope.launch {
                        viewTypeGrid.value = !viewTypeGrid.value
                    }
                }) {
                    Icon(
                        painterResource(id = if (viewTypeGrid.value) R.drawable.baseline_list_24 else R.drawable.baseline_grid_on_24),
                        contentDescription = stringResource(id = R.string.cambiaVista),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    context.findActivity().getPreferences(Context.MODE_PRIVATE)
                        .getString("country", "").orEmpty(),
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            })
        },
        bottomBar = {
            BottomAppBar(actions = {
                IconButton(onClick = {
                    scope.launch {
                        showConfirmDialog = true

                    }
                }) {
                    Icon(
                        painterResource(id = R.drawable.baseline_cloud_upload_24),
                        contentDescription = stringResource(id = R.string.carica_su_catbox),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }, floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showBottomSheet = true
                    },
                ) {
                    Icon(
                        Icons.Default.Add,
                        stringResource(id = R.string.aggiungi_foto),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

            })
        },
    ) { paddings ->
        when (catalogState) {
            is CatalogScreenUiState.Error -> TODO()
            CatalogScreenUiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            is CatalogScreenUiState.Success -> {
                if (viewTypeGrid.value) {
                    CatalogScreenGrid(
                        photos = (catalogState as CatalogScreenUiState.Success).data,
                        modifier = modifier.padding(paddings),
                        openPhoto = openPhoto
                    )
                } else {
                    CatalogScreenList(
                        photos = (catalogState as CatalogScreenUiState.Success).data,
                        modifier = modifier.padding(paddings)
                    )
                }

            }
        }

        if (showBottomSheet) {
            AddContent(
                permissionLauncher,
                cameraLauncher,
                pickMultipleMedia,
                uri,
                sheetState,
                scope,
                context
            ) {
                showBottomSheet = it
            }
        }

        if (showConfirmDialog) {
            UploadConfirmDialog {
                showConfirmDialog = false
                if (it) {
                    scope.launch {
                        viewModel.uploadPhotos()
                    }
                }
            }
        }

    }

    if (uploadFileWithProgress is UploadUIState.Loading) {
        DialogBox {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = stringResource(id = R.string.upload))
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { uploadProgress },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { viewModel.cancelUpload() }) {
                    Icon(
                        Icons.Filled.Clear,
                        contentDescription = stringResource(id = R.string.annulla)
                    )
                }
            }
        }
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddContent(
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    pickMultipleMedia: ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>,
    uri: Uri,
    sheetState: SheetState,
    scope: CoroutineScope,
    context: Context,
    showBottomSheet: (Boolean) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            showBottomSheet(false)
        }, sheetState = sheetState
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                Card(Modifier.clickable {
                    scope.launch {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            // Request a permission
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet(false)
                        }
                    }
                }) {
                    Column(
                        Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painterResource(id = R.drawable.baseline_add_a_photo_24),
                            contentDescription = stringResource(id = R.string.scatta_una_foto)
                        )
                        Text(text = stringResource(id = R.string.scatta_una_foto))
                    }

                }
                Card(Modifier.clickable {
                    scope.launch {
                        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet(false)
                        }
                    }
                }) {
                    Column(
                        Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painterResource(id = R.drawable.baseline_add_photo_alternate_24),
                            contentDescription = stringResource(id = R.string.aggiungi_foto)
                        )
                        Text(text = stringResource(id = R.string.aggiungi_foto))
                    }

                }
            }
        }
    }
}

@Composable
fun CatalogScreenList(photos: List<Catalog>, modifier: Modifier) {
    val context = LocalContext.current
    val localClipboardManager = LocalClipboardManager.current
    LazyColumn(modifier = modifier) {
        itemsIndexed(photos) { index, photo ->
            ListItem(headlineContent = {
                Text(photo.url.orEmpty(), overflow = TextOverflow.Ellipsis)
            }, leadingContent = {
                if (photo.url.isNullOrEmpty()) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painterResource(id = R.drawable.baseline_cloud_queue_24),
                            contentDescription = "icon",
                            Modifier.size(24.dp)
                        )
                    }
                } else {
                    IconButton(onClick = {
                        localClipboardManager.setText(AnnotatedString(photo.url.orEmpty()))
                        Toast.makeText(
                            context, context.getString(R.string.url_copiato), Toast.LENGTH_LONG
                        ).show()
                    }) {
                        Icon(
                            painterResource(id = R.drawable.baseline_content_copy_24),
                            contentDescription = stringResource(id = R.string.copia_url),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            })
            if (index < photos.lastIndex) HorizontalDivider()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun CatalogScreenGrid(
    photos: List<Catalog>, modifier: Modifier = Modifier, openPhoto: (Catalog) -> Unit
) {
    LazyVerticalStaggeredGrid(modifier = modifier,
        columns = StaggeredGridCells.Adaptive(128.dp),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(photos) { photo ->
                Box(Modifier.clickable {
                    openPhoto(photo)
                }) {
                    GlideImage(
                        model = Uri.parse(photo.path), contentDescription = "image"
                    )

                    if (photo.url.isNullOrEmpty()) {
                        Box(
                            Modifier
                                .background(White, CircleShape)
                                .size(width = 25.dp, height = 25.dp)
                                .padding(2.dp),
                            Alignment.BottomEnd
                        ) {
                            Icon(
                                painterResource(id = R.drawable.baseline_cloud_queue_24),
                                contentDescription = "icon",
                                Modifier.size(24.dp)
                            )
                        }

                    }
                }
            }
        })
}

@Composable
fun UploadConfirmDialog(onConfirm: (Boolean) -> Unit) {
    AlertDialog(onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(true)
                }
            ) {
                Text("Procedi")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onConfirm(false)
                }
            ) {
                Text("Annulla")
            }
        },
        title = {
            Text("Upload")
        },
        text = {
            Text("Sicuro di voler procere con l'upload?")
        }
    )
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        //CatalogScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        //CatalogScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}
