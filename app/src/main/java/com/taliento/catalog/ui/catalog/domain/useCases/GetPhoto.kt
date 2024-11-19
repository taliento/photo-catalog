package com.taliento.catalog.ui.catalog.domain.useCases

import com.taliento.catalog.model.Catalog
import com.taliento.catalog.ui.catalog.domain.repository.CatalogRepository
import kotlinx.coroutines.flow.Flow

/**
 * Created by Davide Taliento on 15/11/24.
 */
class GetPhoto(private val repository: CatalogRepository) {
    suspend operator fun invoke(uid: Int): Flow<Catalog?> = repository.getByUid(uid)
}