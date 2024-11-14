package com.taliento.catalog.ui.countries.di

import com.taliento.catalog.network.retrofit.RetrofitPhotoCatalogNetwork
import com.taliento.catalog.ui.countries.data.CountriesRepositoryImpl
import com.taliento.catalog.ui.countries.domain.repository.CountriesRepository
import com.taliento.catalog.ui.countries.domain.useCases.GetCountries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Davide Taliento on 14/11/24.
 */
@Module
@InstallIn(SingletonComponent::class)
object CountriesModule {

    @Provides
    @Singleton
    fun provideCountriesRepository(network: RetrofitPhotoCatalogNetwork) : CountriesRepository = CountriesRepositoryImpl(network)

    @Provides
    @Singleton
    fun provideGetCountriesUseCase(repository: CountriesRepository) = GetCountries(repository)

}