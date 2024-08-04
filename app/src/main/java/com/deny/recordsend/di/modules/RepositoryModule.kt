package com.deny.recordsend.di.modules

import com.cloudinary.android.MediaManager
import com.deny.data.remote.services.ApiService
import com.deny.data.repositories.CloudinaryRepositoryImpl
import com.deny.data.repositories.RepositoryImpl
import com.deny.domain.repositories.CloudinaryRepository
import com.deny.domain.repositories.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    fun provideRepository(apiService: ApiService): Repository = RepositoryImpl(apiService)

    @Provides
    fun provideCloudinaryRepository(mediaManager: MediaManager): CloudinaryRepository {
        return CloudinaryRepositoryImpl(mediaManager)
    }
}
