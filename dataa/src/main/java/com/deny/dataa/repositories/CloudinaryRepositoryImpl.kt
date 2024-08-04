package com.deny.dataa.repositories

import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.deny.dataa.extensions.flowTransform
import com.deny.dataa.remote.models.response.UploadResponse
import com.deny.dataa.remote.models.response.toModel
import com.deny.domain.exceptions.UploadException
import com.deny.domain.models.UploadModel
import com.deny.domain.repositories.CloudinaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CloudinaryRepositoryImpl @Inject constructor(
    private val mediaManager: MediaManager
) : CloudinaryRepository {

    override fun uploadVideo(imageUri: File, uploadPreset: String): Flow<UploadModel> = flowTransform {
        suspendCancellableCoroutine { continuation ->
            mediaManager.upload(imageUri.absolutePath)
                .unsigned(uploadPreset)
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                        // Optionally handle start
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                        // Optionally handle progress
                    }

                    override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                        val uploadResponse = UploadResponse(
                            url = resultData["url"] as? String,
                            publicId = resultData["public_id"] as? String,
                            type = resultData["type"] as? String,
                            resourceType = resultData["resource_type"] as? String,
                            transformation = resultData["transformation"] as? String,
                            assetId = resultData["asset_id"] as? String,
                            displayName = resultData["display_name"] as? String
                        )
                        continuation.resume(uploadResponse.toModel())
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        continuation.resumeWithException(
                            UploadException
                        )
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        // Optionally handle reschedule
                    }
                }).dispatch()

            continuation.invokeOnCancellation {
                // Handle cancellation if needed
            }
        }
    }

}
