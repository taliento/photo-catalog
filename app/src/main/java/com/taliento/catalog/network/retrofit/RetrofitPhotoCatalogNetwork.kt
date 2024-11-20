package com.taliento.catalog.network.retrofit

import androidx.compose.ui.util.trace
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.taliento.catalog.model.Country
import com.taliento.catalog.network.PhotoCatalogNetworkDataSource
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Davide Taliento on 13/11/24.
 */

private const val PHOTOFORSE_URL = "https://api.photoforse.online/"

private const val CATBOX_URL = "https://catbox.moe/"

@Singleton
class RetrofitPhotoCatalogNetwork @Inject constructor(
    networkJson: Json,
    @Named("country") okhttpCallFactory: dagger.Lazy<Call.Factory>,
    @Named("catbox") catboxOkHttpCallFactory: dagger.Lazy<Call.Factory>,
) : PhotoCatalogNetworkDataSource {

    private val photoforseNetworkApi = trace("photoforseNetworkApi") {
        Retrofit.Builder()
            .baseUrl(PHOTOFORSE_URL)
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(PhotoforseApi::class.java)
    }

    private val catboxNetworkApi = trace("catboxNetworkApi") {
        Retrofit.Builder()
            .baseUrl(CATBOX_URL)
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { catboxOkHttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .build()
            .create(CatBoxApi::class.java)
    }

    override suspend fun getCountries(): List<Country> =
        photoforseNetworkApi.getCountries()

    override suspend fun fileUpload(content: ByteArray, fileName: String): String {
        val filePart =
            MultipartBody.Part.createFormData("fileToUpload", fileName, content.toRequestBody())
        return catboxNetworkApi.fileUpload(filePart).string()
    }

}