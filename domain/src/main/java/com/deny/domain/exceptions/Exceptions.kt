package com.deny.domain.exceptions

import com.deny.domain.models.Error

object NoConnectivityException : RuntimeException()

data class ApiException(
    val error: Error?,
    val httpCode: Int,
    val httpMessage: String?
) : RuntimeException()

object UploadException : RuntimeException("Failed to upload video")
