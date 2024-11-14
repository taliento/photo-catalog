package com.taliento.catalog.ui.countries.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taliento.catalog.model.Country
import com.taliento.catalog.ui.countries.domain.useCases.GetCountries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created by Davide Taliento on 14/11/24.
 */
@HiltViewModel
class CountriesViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountries
) : ViewModel() {

    val countriesUiState : StateFlow<CountriesUiState> =
        getCountriesUseCase()
            .map<List<Country>, CountriesUiState>(CountriesUiState::Success)
            .onStart { emit(CountriesUiState.Loading) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CountriesUiState.Loading,
            )

}