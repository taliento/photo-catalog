package com.taliento.catalog.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Created by Davide Taliento on 19/11/24.
 */
@Serializable
@Entity
data class Catalog(
    var path: String,
    var url: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}