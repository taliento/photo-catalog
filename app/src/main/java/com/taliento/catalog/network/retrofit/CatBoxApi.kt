package com.taliento.catalog.network.retrofit

import retrofit2.http.Multipart

/**
 * Created by Davide Taliento on 14/11/24.
 */
interface CatBoxApi {
    @Multipart
    suspend fun fileUpload(content: ByteArray, reqType: String = "fileupload"): String
}