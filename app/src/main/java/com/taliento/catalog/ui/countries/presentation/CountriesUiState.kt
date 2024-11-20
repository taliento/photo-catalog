package com.taliento.catalog.ui.countries.presentation

import com.taliento.catalog.model.Country

/**
 * Created by Davide Taliento on 14/11/24.
 */
sealed interface CountriesUiState {

    data object Loading : CountriesUiState
    data class Error(val throwable: Throwable) : CountriesUiState
    data class Success(val result: List<Country>) : CountriesUiState
}