package com.taliento.catalog.ui.countries.presentation

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taliento.catalog.model.Country
import com.taliento.catalog.utils.findActivity

/**
 * Created by Davide Taliento on 14/11/24.
 */

@Composable
fun CountryScreen(modifier: Modifier, goToCatalog: () -> Unit, viewModel: CountriesViewModel = hiltViewModel()) {
    val countriesUiState by viewModel.countriesUiState.collectAsStateWithLifecycle()
    Column(modifier) {
        when (countriesUiState) {
            CountriesUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            is CountriesUiState.Success -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Benvenuto", fontSize = 30.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    CountrySelect((countriesUiState as CountriesUiState.Success).result, goToCatalog)
                }

            }
        }
    }
}

@Composable
fun CountrySelect(result: List<Country>, goToCatalog: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Button(onClick = { expanded = true }) {
        Text(text = "Seleziona paese")
        Icon(Icons.Filled.ArrowDropDown, contentDescription = "")
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        result.forEach { country ->
            DropdownMenuItem(text = { Text(country.name.orEmpty()) }, onClick = {
                //non ci sono specifiche su come utilizzare questa informazione, salvo il name nelle shared preferences
                val sharedPref = context.findActivity().getPreferences(Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("country", country.name.orEmpty())
                    apply()
                }
                expanded = false
                goToCatalog()
            })
        }

    }
}
