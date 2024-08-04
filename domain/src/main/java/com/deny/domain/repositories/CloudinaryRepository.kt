package com.deny.domain.repositories

import com.deny.domain.models.UploadModel
import kotlinx.coroutines.flow.Flow
import java.io.File

interface CloudinaryRepository {
    fun uploadVideo(imageUri: File, uploadPreset: String): Flow<UploadModel>
}
