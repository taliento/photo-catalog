package com.taliento.catalog.ui.countries.data

import com.taliento.catalog.model.Country
import com.taliento.catalog.network.retrofit.RetrofitPhotoCatalogNetwork
import com.taliento.catalog.ui.countries.domain.repository.CountriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Davide Taliento on 14/11/24.
 */
class CountriesRepositoryImpl(private val network: RetrofitPhotoCatalogNetwork) : CountriesRepository {

    override fun getCountries(): Flow<List<Country>> =
        flow { emit(network.getCountries()) }
}