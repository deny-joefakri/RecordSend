package com.deny.recordsend.di.modules

import com.deny.recordsend.utilities.DispatchersProvider
import com.deny.recordsend.utilities.DispatchersProviderImpl
import com.deny.recordsend.utilities.file_checker.FileHelper
import com.deny.recordsend.utilities.file_checker.FileHelperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideDispatchersProvider(): DispatchersProvider {
        return DispatchersProviderImpl()
    }


}
