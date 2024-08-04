package com.deny.domain.models

data class UploadModel(
    val url: String,
    val secureUrl: String,
    val publicId: String,
    val type: String,
    val resourceType: String,
    val transformation: String,
    val assetId: String,
    val displayName: String
)