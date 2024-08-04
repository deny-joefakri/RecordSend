package com.deny.data.local.services

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deny.domain.models.UploadedVideoEntity

@Database(entities = [UploadedVideoEntity::class], version = 1)
abstract class UploadedVideoDatabase: RoomDatabase() {
    abstract val dao: UploadedVideoDao
}