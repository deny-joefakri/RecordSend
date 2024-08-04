package com.deny.dataa.local.services

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.deny.dataa.local.models.UploadedVideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UploadedVideoDao {
    @Insert
    suspend fun insertVideo(video: UploadedVideoEntity) : Long

    @Query("SELECT * FROM videosuploaded")
    fun getAllVideos(): Flow<List<UploadedVideoEntity>>
}