package com.taliento.catalog.ui.catalog.domain.useCases

import com.taliento.catalog.ui.catalog.domain.repository.CatalogRepository

/**
 * Created by Davide Taliento on 15/11/24.
 */
class GetCatalogPhotosToUpload(private val repository: CatalogRepository) {
    operator fun invoke() = repository.getCatalogPhotosToUpload()
}