package com.deny.dataa.remote.models.response

import com.squareup.moshi.Json
import com.deny.domain.models.Error

data class ErrorResponse(
    @Json(name = "message")
    val message: String
)

internal fun ErrorResponse.toModel() = Error(message = message)
