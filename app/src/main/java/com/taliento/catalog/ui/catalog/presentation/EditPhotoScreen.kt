package com.taliento.catalog.ui.catalog.presentation

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.taliento.catalog.R
import com.taliento.catalog.model.Catalog
import com.taliento.catalog.utils.getBitmap


/**
 * Created by Davide Taliento on 19/11/24.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPhotoScreen(
    uid: Int, goBack: (Catalog) -> Unit, viewModel: CatalogScreenViewModel = hiltViewModel()
) {
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val context = LocalContext.current as Activity

    val uidState by viewModel.getByUidState.collectAsStateWithLifecycle()
    viewModel.setUid(uid)
    when (uidState) {
        is EditScreenUiState.EditSuccess -> {

            val photo = (uidState as EditScreenUiState.EditSuccess).data
            bitmap = context.getBitmap(Uri.parse(photo?.path))

            val imageCropLauncher =
                rememberLauncherForActivityResult(contract = CropImageContract()) { result ->
                    if (result.isSuccessful) {
                        result.uriContent?.let { uri ->

                            bitmap = if (Build.VERSION.SDK_INT < 28) {
                                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                            } else {
                                val source = ImageDecoder.createSource(context.contentResolver, uri)
                                ImageDecoder.decodeBitmap(source)
                            }
                            photo?.path = uri.toString()
                            photo?.let {
                                viewModel.updatePhoto(it)
                            }
                        }

                    }
                }

            Scaffold(topBar = {
                TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ), title = { Text("Modifica") }, navigationIcon = {
                    IconButton(onClick = {
                        photo?.let {
                            goBack(it)
                        }

                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back", tint = MaterialTheme.colorScheme.primary
                        )
                    }
                })
            }, bottomBar = {
                BottomAppBar(actions = {
                    TextButton(onClick = {
                        val cropOptions = CropImageContractOptions(
                            Uri.parse(photo?.path), CropImageOptions()
                        )
                        imageCropLauncher.launch(cropOptions)
                    }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_crop_rotate_24),
                                contentDescription = "crop rotate",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(text = "Crop")
                        }
                    }
                    TextButton(onClick = {
                        //todo
                        Toast.makeText(context, "TODO" , Toast.LENGTH_SHORT).show()
                    }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_tune_24),
                                contentDescription = "crop rotate",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(text = "Filters")
                        }
                    }
                })
            }) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }


}