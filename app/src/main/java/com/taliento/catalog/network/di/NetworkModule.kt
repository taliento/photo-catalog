package com.taliento.catalog.network.di

import androidx.compose.ui.util.trace
import com.taliento.catalog.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * Created by Davide Taliento on 13/11/24.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = trace("PhotoCatalogOkHttpClient") {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        //debugging purpose
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    },
            )
            .addInterceptor(Interceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .header("x-api-key", BuildConfig.PHOTOFORSE_API_KEY)
                    .build()
                chain.proceed(request)
            })
            .build()
    }

}