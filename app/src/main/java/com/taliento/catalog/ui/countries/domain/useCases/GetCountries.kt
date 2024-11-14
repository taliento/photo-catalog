package com.taliento.catalog.ui.countries.domain.useCases

import com.taliento.catalog.ui.countries.domain.repository.CountriesRepository

/**
 * Created by Davide Taliento on 14/11/24.
 */
class GetCountries(private val repository: CountriesRepository) {
    operator fun invoke() = repository.getCountries()
}