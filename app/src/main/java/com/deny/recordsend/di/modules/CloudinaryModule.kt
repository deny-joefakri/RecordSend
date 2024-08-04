package com.deny.recordsend.di.modules

import android.content.Context
import com.cloudinary.android.MediaManager
import com.deny.data.remote.providers.CloudinaryServiceProvider
import com.deny.recordsend.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CloudinaryModule {

    @Provides
    @Singleton
    fun provideMediaManager(
        @ApplicationContext context: Context
    ): MediaManager {
        val config: MutableMap<String, Any> = HashMap()
        config["cloud_name"] = BuildConfig.CLOUD_NAME
        config["secure"] = true
        CloudinaryServiceProvider.initCloudinary(context, config)
        return CloudinaryServiceProvider.getMediaManager()
    }
//
//    @Provides
//    @Singleton
//    fun provideCloudinaryRepository(mediaManager: MediaManager): CloudinaryRepository {
//        return CloudinaryRepositoryImpl(mediaManager)
//    }

//    @Provides
//    @Singleton
//    fun provideUploadVideoUseCase(cloudinaryRepository: CloudinaryRepository): UploadVideoUseCase {
//        return UploadVideoUseCase(cloudinaryRepository)
//    }

}