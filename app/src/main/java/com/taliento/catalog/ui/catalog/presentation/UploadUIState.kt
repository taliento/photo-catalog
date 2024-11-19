package com.taliento.catalog.ui.catalog.presentation

/**
 * Created by Davide Taliento on 16/11/24.
 */
sealed interface UploadUIState {
    data object NotLoading : UploadUIState
    data object Loading : UploadUIState
    data object Success : UploadUIState
}