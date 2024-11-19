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

package com.taliento.catalog.ui.catalog


import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.taliento.catalog.PhotoCatalog
import com.taliento.catalog.model.Catalog
import com.taliento.catalog.ui.catalog.domain.repository.CatalogRepository
import com.taliento.catalog.ui.catalog.domain.useCases.UploadPhoto
import com.taliento.catalog.ui.catalog.presentation.CatalogScreenUiState
import com.taliento.catalog.ui.catalog.presentation.CatalogScreenViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class CatalogScreenViewModelTest {

    lateinit var context: Context
    lateinit var catalogScreenViewModel: CatalogScreenViewModel
    lateinit var uploadPhotoUseCase: UploadPhoto
    var catalogRepository = FakeCatalogRepository()

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext<PhotoCatalog>().applicationContext
        uploadPhotoUseCase = UploadPhoto(catalogRepository)
        catalogScreenViewModel = CatalogScreenViewModel(context = context, catalogRepository, uploadPhotoUseCase)
    }

    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel =
        assertEquals(catalogScreenViewModel.uiState.first(), CatalogScreenUiState.Loading)
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        assertEquals(catalogScreenViewModel.uiState.first(), CatalogScreenUiState.Loading)
    }
}

class FakeCatalogRepository : CatalogRepository {

    private val data = mutableListOf<Catalog>()

    override val catalogPhotos: Flow<List<Catalog>>
        get() = flow { emit(data.toList()) }

    override val toUpload: Flow<List<Catalog>>
        get() = flow { emit(listOf()) }


    override suspend fun add(path: String) {
        data.add(0, Catalog(path))
    }

    override suspend fun update(catalog: Catalog) {
        //TODO
    }

    override suspend fun delete(photo: Catalog) {
        //TODO
    }

    override suspend fun getByUid(uid: Int): Flow<Catalog> {
        TODO("Not yet implemented")
    }

    override suspend fun fileUpload(content: ByteArray, fileName: String): String {
        return "url"
    }
}