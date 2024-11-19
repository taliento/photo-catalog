package com.taliento.catalog.ui.catalog.presentation

import com.taliento.catalog.model.Catalog

/**
 * Created by Davide Taliento on 19/11/24.
 */
interface EditScreenUiState {
    object Loading : EditScreenUiState
    data class Error(val throwable: Throwable) : EditScreenUiState
    data class EditSuccess(val data: Catalog?) : EditScreenUiState
}