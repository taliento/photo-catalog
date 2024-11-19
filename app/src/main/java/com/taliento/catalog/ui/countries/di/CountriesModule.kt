package com.taliento.catalog.ui.countries.di

import com.taliento.catalog.ui.countries.data.CountriesRepositoryImpl
import com.taliento.catalog.ui.countries.domain.repository.CountriesRepository
import com.taliento.catalog.ui.countries.domain.useCases.GetCountries
import dagger.Binds
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
interface CountriesDataModule {

    @Singleton
    @Binds
    fun provideCountriesRepository(repositoryImpl: CountriesRepositoryImpl): CountriesRepository

}

@Module
@InstallIn(SingletonComponent::class)
object CountriesModule {

    @Provides
    @Singleton
    fun provideGetCountriesUseCase(repository: CountriesRepository) = GetCountries(repository)

}