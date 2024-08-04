package com.deny.recordsend.ui.screen.main.dashboard

import android.net.Uri
import com.deny.domain.models.UploadModel

data class DashboardUIState(
    val doFileExists: Boolean = false,
    val fileUri: Uri? = null
)
