package com.deny.recordsend.ui.screen.main.record

import android.content.Context
import androidx.camera.video.Recording
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toFile
import androidx.lifecycle.viewModelScope
import com.deny.domain.models.UploadModel
import com.deny.domain.models.UploadStatus
import com.deny.domain.models.UploadedVideoEntity
import com.deny.domain.repositories.LocalRepository
import com.deny.domain.usecases.UploadVideoUseCase
import com.deny.recordsend.ui.base.BaseViewModel
import com.deny.recordsend.utilities.DispatchersProvider
import com.deny.recordsend.utilities.FILE_NAME
import com.deny.recordsend.utilities.file_checker.FileHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val uploadVideoUseCase: UploadVideoUseCase,
    private val localRepository: LocalRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val fileChecker: FileHelper
) : BaseViewModel(){

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState = _uiState.asStateFlow()

    private val _uploadStatus = MutableStateFlow<UploadStatus>(UploadStatus.Idle)
    val uploadStatus: StateFlow<UploadStatus> = _uploadStatus.asStateFlow()

    private val mContext by lazy { context }
    private var isPaused = false

    fun uploadVideo(file: File) {
        Timber.e("file.name ${file.name}")
        uploadVideoUseCase(file)
            .flowOn(dispatchersProvider.io)
            .onEach { status ->
                _uploadStatus.emit(status)
                if (status is UploadStatus.Success) {
                    saveVideoToLocalDb(status.result)
                }
            }
            .catch { exception ->
                _uploadStatus.emit(UploadStatus.Error(exception))
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

    fun startRecording() {
        _uiState.value = _uiState.value.copy(
            isRecording = true,
            showRec = true,
            showTime = true,
            showStopButton = true
        )
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (_uiState.value.isRecording && !isPaused) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    recordedDuration = _uiState.value.recordedDuration + 1000000000L // 1 second in nanoseconds
                )
            }
        }
    }

    fun stopRecording() {
        _uiState.value = _uiState.value.copy(isRecording = false, showRec = false)
    }

    fun pauseRecording() {
        isPaused = true
        _uiState.value = _uiState.value.copy(isRecording = false, showRec = false)
    }

    fun resumeRecording() {
        isPaused = false
        _uiState.value = _uiState.value.copy(isRecording = true, showRec = true)
        startTimer()
    }

    fun getPause() = isPaused
    fun setPause(value : Boolean) {
        isPaused = value
    }
}
