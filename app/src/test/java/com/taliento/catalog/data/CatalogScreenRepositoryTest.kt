/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taliento.catalog.data

import com.taliento.catalog.data.local.database.CatalogDao
import com.taliento.catalog.model.Catalog
import com.taliento.catalog.model.Country
import com.taliento.catalog.network.PhotoCatalogNetworkDataSource
import com.taliento.catalog.ui.catalog.data.CatalogRepositoryImpl
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [CatalogRepositoryImpl].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class CatalogScreenRepositoryTest {

    private lateinit var repositoryImpl: CatalogRepositoryImpl

    @Before
    fun setup() {
        repositoryImpl = CatalogRepositoryImpl(FakeNetwork(), FakeCatalogScreenDao())
    }

    @Test
    fun catalogScreens_newItemSaved_itemIsReturned() = runTest {

        repositoryImpl.add("Repository")

        assertEquals(repositoryImpl.catalogPhotos.first().size, 1)
    }

}

private class FakeNetwork : PhotoCatalogNetworkDataSource {
    override suspend fun getCountries(): List<Country> {
        TODO("Not yet implemented")
    }

    override suspend fun fileUpload(content: ByteArray, fileName: String): String {
        TODO("Not yet implemented")
    }

}

private class FakeCatalogScreenDao : CatalogDao {

    private val data = mutableListOf<Catalog>()

    override fun getCatalog(): Flow<List<Catalog>> = flow {
        emit(data)
    }

    override fun toUpload(): Flow<List<Catalog>> {
        return flow { emit(listOf()) }
    }

    override fun getByUid(uid: String): Catalog {
        TODO("Not yet implemented")
    }

    override suspend fun insertCatalog(item: Catalog) {
        data.add(0, item)
    }

    override suspend fun updateCatalog(item: Catalog) {
        //TODO
    }

    override suspend fun deleteCatalog(photo: Catalog) {
        //TODO
    }
}