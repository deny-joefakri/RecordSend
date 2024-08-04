package com.deny.dataa.local.repositories

import com.deny.dataa.local.models.UploadedVideoEntity
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun insertVideo(video: UploadedVideoEntity) : Long

    fun getAllVideos(): Flow<List<UploadedVideoEntity>>
}