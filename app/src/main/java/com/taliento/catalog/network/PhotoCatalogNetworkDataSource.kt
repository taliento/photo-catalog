package com.taliento.catalog.network

import com.taliento.catalog.model.Country

interface PhotoCatalogNetworkDataSource {
    suspend fun getCountries(): List<Country>
}