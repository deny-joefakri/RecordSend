package com.deny.recordsend.ui.screen.main.dashboard

import androidx.core.net.toFile
import androidx.lifecycle.viewModelScope
import com.deny.domain.models.UploadedVideoEntity
import com.deny.domain.models.UploadModel
import com.deny.domain.repositories.LocalRepository
import com.deny.domain.usecases.UploadVideoUseCase
import com.deny.recordsend.ui.base.BaseViewModel
import com.deny.recordsend.ui.screen.main.MainDestination
import com.deny.recordsend.utilities.DispatchersProvider
import com.deny.recordsend.utilities.FILE_NAME
import com.deny.recordsend.utilities.file_checker.FileHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    /*private val uploadVideoUseCase: UploadVideoCloudinaryUseCase,*/
    private val uploadVideoUseCase: UploadVideoUseCase,
    private val localRepository: LocalRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val fileChecker: FileHelper
) : BaseViewModel() {

    private val _uploadStatus = MutableStateFlow<UploadStatus>(UploadStatus.Idle)
    val uploadStatus = _uploadStatus.asStateFlow()

    private val _dashboardState = MutableStateFlow(DashboardUIState())
    val dashboardState = _dashboardState.asStateFlow()

    init {
        checkFileAndSetState()
    }

    private fun checkFileAndSetState() {
        if (fileChecker.doFileExists(FILE_NAME)) {
            val uri = fileChecker.getFileUri(FILE_NAME)
            _dashboardState.update {
                it.copy(
                    doFileExists = true,
                    fileUri = uri
                )
            }
        } else {
            _dashboardState.update {
                it.copy(
                    doFileExists = false,
                    fileUri = null
                )
            }
        }
    }

    fun uploadVideo() {
        val uri = fileChecker.getFileUri(FILE_NAME)
        uploadVideoUseCase(uri.toFile())
            .injectLoading()
            .onEach {
                _uploadStatus.emit(UploadStatus.Success(it))
                saveVideoToLocalDb(it)
            }
            .flowOn(dispatchersProvider.io)
            .catch {
                _uploadStatus.emit(UploadStatus.Error(it))
            }
            .launchIn(viewModelScope)

    }

    private fun saveVideoToLocalDb(uploadModel: UploadModel) {
        viewModelScope.launch(dispatchersProvider.io) {
            val videoEntity = UploadedVideoEntity(
                url = uploadModel.url,
                publicId = uploadModel.publicId,
                type = uploadModel.type, // assuming type is always video
                resourceType = uploadModel.resourceType, // assuming resourceType is always video
                transformation = uploadModel.transformation,
                assetId = uploadModel.assetId,
                displayName = uploadModel.displayName,
                secureUrl = uploadModel.secureUrl
            )
            localRepository.insertVideo(videoEntity)
        }
    }

    fun updateState() {
        checkFileAndSetState()
    }

    fun navigateToRecording() {
        launch { _navigator.emit(MainDestination.Record) }
    }

    fun navigateToList() {
        launch { _navigator.emit(MainDestination.List) }
    }

}
