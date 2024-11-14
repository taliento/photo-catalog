package com.taliento.catalog.network.di

import com.taliento.catalog.network.PhotoCatalogNetworkDataSource
import com.taliento.catalog.network.retrofit.RetrofitPhotoCatalogNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Davide Taliento on 14/11/24.
 */
@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun binds(impl: RetrofitPhotoCatalogNetwork): PhotoCatalogNetworkDataSource
}