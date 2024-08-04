package com.deny.dataa.local.services

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deny.dataa.local.models.UploadedVideoEntity
import com.deny.dataa.local.services.UploadedVideoDao

@Database(entities = [UploadedVideoEntity::class], version = 1)
abstract class UploadedVideoDatabase: RoomDatabase() {
    abstract val dao: UploadedVideoDao
}