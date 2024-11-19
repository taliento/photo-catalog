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

package com.taliento.catalog.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.taliento.catalog.model.Catalog
import kotlinx.coroutines.flow.Flow


@Dao
interface CatalogDao {
    @Query("SELECT * FROM catalog ORDER BY uid DESC")
    fun getCatalog(): Flow<List<Catalog>>

    @Query("SELECT * FROM catalog WHERE url is null ORDER BY uid DESC")
    fun toUpload(): Flow<List<Catalog>>

    @Query("SELECT * FROM catalog WHERE uid = :uid")
    fun getByUid(uid: String): Catalog

    @Insert
    suspend fun insertCatalog(item: Catalog)

    @Update
    suspend fun updateCatalog(item: Catalog)

    @Delete
    suspend fun deleteCatalog(photo: Catalog)
}