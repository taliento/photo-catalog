package com.taliento.catalog.model

import kotlinx.serialization.Serializable

/**
 * Created by Davide Taliento on 13/11/24.
 */
@Serializable
data class Country(
    val iso: Int?,
    val isoAlpha2: String?,
    val isoAlpha3: String?,
    val name: String?,
    val phonePrefix: String?
)