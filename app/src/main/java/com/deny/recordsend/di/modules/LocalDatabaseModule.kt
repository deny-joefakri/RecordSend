package com.deny.recordsend.di.modules

import android.app.Application
import androidx.room.Room
import com.deny.data.local.services.UploadedVideoDao
import com.deny.data.local.services.UploadedVideoDatabase
import com.deny.data.repositories.LocalRepositoryImpl
import com.deny.domain.repositories.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDatabaseModule {

    @Provides
    @Singleton
    fun providesUploadedVideoDatabase(app: Application): UploadedVideoDatabase =
        Room.databaseBuilder(app, UploadedVideoDatabase::class.java, "uploadedvideo_db").build()

    @Provides
    fun provideUploadedVideoDao(database: UploadedVideoDatabase): UploadedVideoDao {
        return database.dao
    }

    @Provides
    @Singleton
    fun providesUploadedVideoRepository(uploadedVideoDao: UploadedVideoDao): LocalRepository = LocalRepositoryImpl(uploadedVideoDao = uploadedVideoDao)
}