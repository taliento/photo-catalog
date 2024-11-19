package com.taliento.catalog.ui.catalog.presentation

import com.taliento.catalog.model.Catalog

/**
 * Created by Davide Taliento on 16/11/24.
 */

sealed interface CatalogScreenUiState {
    object Loading : CatalogScreenUiState
    data class Error(val throwable: Throwable) : CatalogScreenUiState
    data class Success(val data: List<Catalog>) : CatalogScreenUiState
}