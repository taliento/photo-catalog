package com.taliento.catalog.ui.catalog.di

import com.taliento.catalog.ui.catalog.data.CatalogRepositoryImpl
import com.taliento.catalog.ui.catalog.domain.repository.CatalogRepository
import com.taliento.catalog.ui.catalog.domain.useCases.AddPhoto
import com.taliento.catalog.ui.catalog.domain.useCases.DeletePhoto
import com.taliento.catalog.ui.catalog.domain.useCases.GetCatalogPhotos
import com.taliento.catalog.ui.catalog.domain.useCases.GetCatalogPhotosToUpload
import com.taliento.catalog.ui.catalog.domain.useCases.GetPhoto
import com.taliento.catalog.ui.catalog.domain.useCases.UpdatePhoto
import com.taliento.catalog.ui.catalog.domain.useCases.UploadPhoto
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Davide Taliento on 15/11/24.
 */
@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun provideCatalogRepository(catalogoRepositoryImpl: CatalogRepositoryImpl): CatalogRepository
}

@Module
@InstallIn(SingletonComponent::class)
object CatalogModule {

    @Singleton
    @Provides
    fun provideUploadPhotoUseCase(repository: CatalogRepository) = UploadPhoto(repository)

    @Singleton
    @Provides
    fun provideAddPhotoUseCase(repository: CatalogRepository) = AddPhoto(repository)

    @Singleton
    @Provides
    fun provideDeletePhotoUseCase(repository: CatalogRepository) = DeletePhoto(repository)

    @Singleton
    @Provides
    fun provideGetPhotoUseCase(repository: CatalogRepository) = GetPhoto(repository)

    @Singleton
    @Provides
    fun provideUpdatePhotoUseCase(repository: CatalogRepository) = UpdatePhoto(repository)

    @Singleton
    @Provides
    fun provideGetCatalogPhotosUseCase(repository: CatalogRepository) = GetCatalogPhotos(repository)

    @Singleton
    @Provides
    fun provideGetCatalogPhotosToUploadUseCase(repository: CatalogRepository) =
        GetCatalogPhotosToUpload(repository)
}