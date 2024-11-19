package com.taliento.catalog.ui.catalog.presentation

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.taliento.catalog.R
import com.taliento.catalog.model.Catalog

/**
 * Created by Davide Taliento on 18/11/24.
 */
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    photo: Catalog,
    goToEditScreen: () -> Unit,
    goBack: () -> Unit,
    viewModel: CatalogScreenViewModel = hiltViewModel()
) {
    var showConfirmDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(topBar = {
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = { Text(text = "Dettaglio") }, navigationIcon = {
            IconButton(onClick = { goBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back", tint = MaterialTheme.colorScheme.primary)
            }
        })
    }, bottomBar = {
        BottomAppBar(actions = {
            TextButton(onClick = { goToEditScreen() }) {
                Text(text = stringResource(id = R.string.modifica))
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                showConfirmDialog = true
            }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        })
    }) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {
            GlideImage(model = Uri.parse(photo.path), contentDescription = "image")
        }


        if (showConfirmDialog) {
            DeleteConfirmDialog {
                showConfirmDialog = false
                if (it) {
                    viewModel.deletePhoto(photo)
                    goBack()
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmDialog(onConfirm: (Boolean) -> Unit) {
    AlertDialog(onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(true)
                }
            ) {
                Text("Elimina", color = MaterialTheme.colorScheme.error)
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
            Text("Elimina")
        },
        text = {
            Text("Sicuro di voler eliminare?")
        }
    )
}