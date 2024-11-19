package com.taliento.catalog.ui.catalog.domain.useCases

import com.taliento.catalog.ui.catalog.domain.repository.CatalogRepository

/**
 * Created by Davide Taliento on 15/11/24.
 */
class UploadPhoto(private val repository: CatalogRepository) {
    suspend operator fun invoke(content: ByteArray, fileName: String): String =
        repository.fileUpload(content, fileName)
}