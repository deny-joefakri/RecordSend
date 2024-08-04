package com.deny.dataa.repositories

import com.deny.dataa.extensions.flowTransform
import com.deny.dataa.remote.models.response.toModel
import com.deny.dataa.remote.services.ApiService
import com.deny.domain.models.UploadModel
import com.deny.domain.repositories.Repository
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RepositoryImpl constructor(
    private val apiService: ApiService
) : Repository {

    override fun uploadVideo(fileUri: File): Flow<UploadModel> = flowTransform {
        val requestBody = fileUri.asRequestBody("video/mp4".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", fileUri.name, requestBody)
        val uploadPresetPart = "android_sample".toRequestBody(MultipartBody.FORM)
        val apiKeyPart = "915474367897474".toRequestBody(MultipartBody.FORM)
        val publicIdPart = "Tesitngn".toRequestBody(MultipartBody.FORM)
        apiService.uploadVideo(filePart, uploadPresetPart, apiKeyPart, publicIdPart).toModel()

    }

}
