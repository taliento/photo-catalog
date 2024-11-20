package com.taliento.catalog.ui.catalog.data

import android.util.Log
import com.taliento.catalog.data.local.database.CatalogDao
import com.taliento.catalog.model.Catalog
import com.taliento.catalog.network.PhotoCatalogNetworkDataSource
import com.taliento.catalog.ui.catalog.domain.repository.CatalogRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Davide Taliento on 15/11/24.
 */
class CatalogRepositoryImpl @Inject constructor(
    private val network: PhotoCatalogNetworkDataSource,
    private val catalogScreenDao: CatalogDao
) : CatalogRepository {
    override suspend fun fileUpload(content: ByteArray, fileName: String): String =
        network.fileUpload(content, fileName)

    override fun getCatalogPhotos(): Flow<List<Catalog>> = catalogScreenDao.getCatalog()

    override fun getCatalogPhotosToUpload(): Flow<List<Catalog>> = catalogScreenDao.toUpload()

    override suspend fun add(path: String) {
        try {
            catalogScreenDao.insertCatalog(Catalog(path = path))
        } catch (exception: Exception) {
            Log.e("CatalogDao", "Failed to add photo", exception)
        }
    }

    override suspend fun update(catalog: Catalog) {
        try {
            catalogScreenDao.updateCatalog(catalog)
        } catch (exception: Exception) {
            Log.e("CatalogDao", "Failed to update photo", exception)
        }

    }

    override suspend fun delete(photo: Catalog) {
        try {
            catalogScreenDao.deleteCatalog(photo)
        } catch (exception: Exception) {
            Log.e("CatalogDao", "Failed to delete photo", exception)
        }
    }

    override suspend fun getByUid(uid: Int): Flow<Catalog?> {
        return flow { emit(catalogScreenDao.getByUid(uid)) }.flowOn(IO)
    }
}