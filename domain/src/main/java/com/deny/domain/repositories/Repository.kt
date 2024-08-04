package com.deny.domain.repositories

import com.deny.domain.models.UploadModel
import com.deny.domain.models.UploadStatus
import kotlinx.coroutines.flow.Flow
import java.io.File

interface Repository {
    fun uploadVideo(fileUri: File): Flow<UploadStatus>
}
