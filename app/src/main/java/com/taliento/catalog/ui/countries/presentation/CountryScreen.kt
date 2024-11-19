package com.taliento.catalog.ui.countries.presentation

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.taliento.catalog.utils.findActivity

/**
 * Created by Davide Taliento on 14/11/24.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryScreen(goToCatalog: () -> Unit, viewModel: CountriesViewModel = hiltViewModel()) {
    val countriesList by viewModel.countriesUiState.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val context = LocalContext.current

    Scaffold(topBar = {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchText,
                    onQueryChange = viewModel::onSearchTextChange,
                    onSearch = {},
                    expanded = false,
                    onExpandedChange = {},
                    placeholder = { Text("Cerca...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                )
            },
            expanded = false,
            onExpandedChange = {},
        ) {}
    }) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            when (countriesList) {
                CountriesUiState.Loading -> {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(64.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                }

                is CountriesUiState.Success -> {
                    val countries = (countriesList as CountriesUiState.Success).result
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        countries.forEachIndexed { index, country ->
                            ListItem(headlineContent = { Text(country.name.orEmpty()) },
                                supportingContent = { Text(country.isoAlpha2.orEmpty()) },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                modifier = Modifier
                                    .clickable {
                                        val sharedPref = context
                                            .findActivity()
                                            .getPreferences(Context.MODE_PRIVATE)
                                        with(sharedPref.edit()) {
                                            putString("country", country.name.orEmpty())
                                            apply()
                                        }
                                        goToCatalog()
                                    }
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp))
                            if (index < countries.lastIndex)
                                HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
