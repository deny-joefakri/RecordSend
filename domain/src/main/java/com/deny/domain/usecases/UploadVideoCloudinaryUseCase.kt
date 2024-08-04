package com.deny.domain.usecases

import com.deny.domain.models.UploadModel
import com.deny.domain.repositories.CloudinaryRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class UploadVideoCloudinaryUseCase @Inject constructor(private val repository: CloudinaryRepository) {
    operator fun invoke(imageUri: File, uploadPreset: String): Flow<UploadModel> {
        return repository.uploadVideo(imageUri, uploadPreset)
    }
}