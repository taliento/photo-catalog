package com.taliento.catalog.network.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Created by Davide Taliento on 14/11/24.
 */
interface CatBoxApi {

    @Multipart
    @POST(value = "user/api.php")
    suspend fun fileUpload(
        @Part fileToUpload: MultipartBody.Part,
        @Part("reqtype") reqtype: RequestBody = "fileupload".toRequestBody()
    ): ResponseBody
}