/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taliento.catalog.ui.catalog

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taliento.catalog.ui.theme.MyApplicationTheme
import com.taliento.catalog.utils.findActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    modifier: Modifier = Modifier, viewModel: CatalogScreenViewModel = hiltViewModel()
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current


    Scaffold(topBar = {
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ), title = {
            Text("Catalogo")
        }, actions = {
            //mostro la selezione del country qua
            Text(
                context.findActivity().getPreferences(Context.MODE_PRIVATE).getString("country", "")
                    .orEmpty(), fontSize = 15.sp
            )
        })
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = { },
        ) {
            Icon(Icons.Filled.Add, "Floating action button.")
        }
    }) { paddings ->
        if (items is CatalogScreenUiState.Success) {
            CatalogScreen(
                items = (items as CatalogScreenUiState.Success).data,
                onSave = viewModel::addCatalogScreen,
                modifier = modifier.padding(paddings)
            )
        }
    }


}

@Composable
internal fun CatalogScreen(
    items: List<String>, onSave: (name: String) -> Unit, modifier: Modifier = Modifier
) {

}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        CatalogScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        CatalogScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}
