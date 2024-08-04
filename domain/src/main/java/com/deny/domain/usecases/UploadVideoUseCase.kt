package com.deny.domain.usecases

import com.deny.domain.models.UploadModel
import com.deny.domain.models.UploadStatus
import com.deny.domain.repositories.Repository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class UploadVideoUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(imageUri: File): Flow<UploadStatus> {
        return repository.uploadVideo(imageUri)
    }
}
