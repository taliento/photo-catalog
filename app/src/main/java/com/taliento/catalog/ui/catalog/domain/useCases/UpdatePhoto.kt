package com.taliento.catalog.ui.catalog.domain.useCases

import com.taliento.catalog.model.Catalog
import com.taliento.catalog.ui.catalog.domain.repository.CatalogRepository

/**
 * Created by Davide Taliento on 15/11/24.
 */
class UpdatePhoto(private val repository: CatalogRepository) {
    suspend operator fun invoke(photo: Catalog) = repository.update(photo)
}