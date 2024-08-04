package com.deny.recordsend.ui.screen.main.dashboard

import android.net.Uri
import com.deny.domain.models.UploadModel

data class DashboardUIState(
    val doFileExists: Boolean = false,
    val fileUri: Uri? = null
)

sealed class UploadStatus {
    object Idle : UploadStatus()
    object Loading : UploadStatus()
    data class Progress(val percentage: Int) : UploadStatus()
    data class Success(val result: UploadModel) : UploadStatus()
    data class Error(val exception: Throwable) : UploadStatus()
}