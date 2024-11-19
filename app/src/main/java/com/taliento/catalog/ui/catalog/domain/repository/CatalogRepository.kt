package com.taliento.catalog.ui.catalog.domain.repository

import com.taliento.catalog.model.Catalog
import kotlinx.coroutines.flow.Flow

/**
 * Created by Davide Taliento on 15/11/24.
 */
interface CatalogRepository {
    val catalogPhotos: Flow<List<Catalog>>

    val toUpload: Flow<List<Catalog>>

    suspend fun add(path: String)

    suspend fun update(catalog: Catalog)

    suspend fun delete(photo: Catalog)

    suspend fun getByUid(uid: Int): Flow<Catalog?>

    suspend fun fileUpload(content: ByteArray, fileName: String): String
}