package com.deny.recordsend.di.modules

import com.deny.recordsend.utilities.file_checker.FileHelper
import com.deny.recordsend.utilities.file_checker.FileHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface UtilitiesModule {

    @Binds
    fun bindFileChecker(fileCheckerImpl: FileHelperImpl): FileHelper

}
