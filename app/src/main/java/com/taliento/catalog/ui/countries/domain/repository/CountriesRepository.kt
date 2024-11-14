package com.taliento.catalog.ui.countries.domain.repository

import com.taliento.catalog.model.Country
import kotlinx.coroutines.flow.Flow

/**
 * Created by Davide Taliento on 14/11/24.
 */
interface CountriesRepository {
    fun getCountries(): Flow<List<Country>>
}