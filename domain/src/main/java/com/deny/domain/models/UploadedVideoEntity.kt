package com.deny.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "videosuploaded")
class UploadedVideoEntity(
    val url: String,
    val secureUrl: String,
    val publicId: String,
    val type: String,
    val resourceType: String,
    val transformation: String,
    val assetId: String,
    val displayName: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}