package com.deny.domain.models

sealed class UploadStatus {
    object Idle : UploadStatus()
    object Loading : UploadStatus()
    data class Progress(val percentage: Int) : UploadStatus()
    data class Success(val result: UploadModel) : UploadStatus()
    data class Error(val exception: Throwable) : UploadStatus()
}