package com.deny.dataa.remote.models.response

import com.deny.domain.models.UploadModel
import com.squareup.moshi.Json

data class UploadResponse(
    @Json(name = "url")
    val url: String?,
    @Json(name = "public_id")
    val publicId: String?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "resource_type")
    val resourceType: String?,
    @Json(name = "transformation")
    val transformation: String?,
    @Json(name = "asset_id")
    val assetId: String?,
    @Json(name = "display_name")
    val displayName: String?,
)

fun UploadResponse.toModel() = UploadModel(
    url = this.url.orEmpty(),
    publicId = this.publicId.orEmpty(),
    type = this.type.orEmpty(),
    resourceType = this.resourceType.orEmpty(),
    transformation = this.transformation.orEmpty(),
    assetId = this.assetId.orEmpty(),
    displayName = this.displayName.orEmpty()
)

