package com.taliento.catalog.network.retrofit

import com.taliento.catalog.model.Country
import retrofit2.http.GET

/**
 * Created by Davide Taliento on 13/11/24.
 */


interface PhotoforseApi {
    @GET(value = "geographics/countries/")
    suspend fun getCountries(): List<Country>
}