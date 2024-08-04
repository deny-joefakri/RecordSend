package com.deny.domain.repositories

import com.deny.domain.models.UploadedVideoEntity
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun insertVideo(video: UploadedVideoEntity) : Long

    fun getAllVideos(): Flow<List<UploadedVideoEntity>>

    fun getVideo(id : Int): Flow<UploadedVideoEntity>
}