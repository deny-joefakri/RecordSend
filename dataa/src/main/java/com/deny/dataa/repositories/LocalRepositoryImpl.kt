package com.deny.dataa.repositories

import com.deny.dataa.local.models.UploadedVideoEntity
import com.deny.dataa.local.services.UploadedVideoDao
import com.deny.dataa.local.services.UploadedVideoDatabase
import com.deny.dataa.local.repositories.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    uploadedVideoDatabase: UploadedVideoDatabase
) : LocalRepository {

    private val dao: UploadedVideoDao = uploadedVideoDatabase.dao

    override suspend fun insertVideo(video: UploadedVideoEntity): Long {
        return dao.insertVideo(video = video)
    }

    override fun getAllVideos(): Flow<List<UploadedVideoEntity>> {
        return dao.getAllVideos()
    }

}
