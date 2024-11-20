package com.taliento.catalog.ui.countries.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taliento.catalog.model.Country
import com.taliento.catalog.ui.countries.domain.useCases.GetCountries
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created by Davide Taliento on 14/11/24.
 */
@HiltViewModel
class CountriesViewModel @Inject constructor(
    getCountriesUseCase: GetCountries
) : ViewModel() {

    //first state whether the search is happening or not
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToogleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }

    val countriesUiState: StateFlow<CountriesUiState> =
        searchText.combine(getCountriesUseCase()) { text, countries ->
            if (text.isEmpty()) {
                countries
            } else {
                countries.filter { it.name?.uppercase()?.contains(text.trim().uppercase()) == true }
            }
        }.map<List<Country>, CountriesUiState>(CountriesUiState::Success)
            .catch {
                Log.e("GetCountries" , it.message.toString())
                emit(CountriesUiState.Error(it))
            }
            .onStart { emit(CountriesUiState.Loading) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CountriesUiState.Loading,
            )

}