package com.deny.data.repositories

import com.deny.data.remote.models.request.ProgressRequestBody
import com.deny.data.remote.models.response.toModel
import com.deny.data.remote.services.ApiService
import com.deny.domain.models.UploadModel
import com.deny.domain.models.UploadStatus
import com.deny.domain.repositories.Repository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RepositoryImpl constructor(
    private val apiService: ApiService
) : Repository {

    override fun uploadVideo(fileUri: File): Flow<UploadStatus> = channelFlow {
        send(UploadStatus.Progress(0)) // Initial progress

        val requestBody = ProgressRequestBody(fileUri, "video/mp4") { progress ->
            launch { send(UploadStatus.Progress(progress)) }
        }

        val filePart = MultipartBody.Part.createFormData("file", fileUri.name, requestBody)
        val uploadPresetPart = "android_sample".toRequestBody(MultipartBody.FORM)
        val apiKeyPart = "915474367897474".toRequestBody(MultipartBody.FORM)
        val publicIdPart = fileUri.name.toRequestBody(MultipartBody.FORM)

        try {
            // Upload the file
            val response = apiService.uploadVideo(filePart, uploadPresetPart, apiKeyPart, publicIdPart)
            send(UploadStatus.Success(response.toModel()))
        } catch (e: Exception) {
            send(UploadStatus.Error(e))
        }

        awaitClose { /* Channel cleanup if needed */ }
    }

}
