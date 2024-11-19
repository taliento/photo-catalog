package com.taliento.catalog.network

import com.taliento.catalog.model.Country

/**
 * Created by Davide Taliento on 13/11/24.
 */
interface PhotoCatalogNetworkDataSource {
    suspend fun getCountries(): List<Country>

    suspend fun fileUpload(content: ByteArray, fileName: String): String
}