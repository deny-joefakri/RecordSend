package com.deny.data.repositories

import com.deny.domain.models.UploadedVideoEntity
import com.deny.data.local.services.UploadedVideoDao
import com.deny.data.local.services.UploadedVideoDatabase
import com.deny.domain.repositories.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val uploadedVideoDao: UploadedVideoDao
) : LocalRepository {


    override suspend fun insertVideo(video: UploadedVideoEntity): Long {
        return uploadedVideoDao.insertVideo(video = video)
    }

    override fun getAllVideos(): Flow<List<UploadedVideoEntity>> {
        return uploadedVideoDao.getAllVideos()
    }

    override fun getVideo(id: Int): Flow<UploadedVideoEntity> {
        return uploadedVideoDao.getVideoById(id)
    }

}
