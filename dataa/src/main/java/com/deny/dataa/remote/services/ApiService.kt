package com.deny.dataa.remote.services

import com.deny.dataa.remote.models.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("video/upload")
    suspend fun uploadVideo(
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: RequestBody,
        @Part("api_key") apiKey: RequestBody,
        @Part("public_id") publicId: RequestBody
    ): UploadResponse
}
