package com.deny.recordsend.ui.screen.main.record

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.camera.core.CameraSelector
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.deny.domain.models.UploadStatus
import com.deny.recordsend.extensions.isPermissionGranted
import com.deny.recordsend.extensions.showToast
import com.deny.recordsend.lib.IsLoading
import com.deny.recordsend.ui.base.BaseDestination
import com.deny.recordsend.ui.base.BaseScreen
import com.deny.recordsend.ui.base.KeyUpdated
import com.deny.recordsend.ui.common.CameraPreview
import com.deny.recordsend.utilities.CAMERA_PERMISSION
import com.deny.recordsend.utilities.FILE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

@Composable
fun RecordScreen(
    viewModel: RecordViewModel = hiltViewModel(),
    navigator: (destination: BaseDestination) -> Unit,
) = BaseScreen(
    isDarkStatusBarIcons = false,
) {
    val context = LocalContext.current
    val isLoading: IsLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val uploadStatus by viewModel.uploadStatus.collectAsStateWithLifecycle()

    when (uploadStatus) {
        is UploadStatus.Progress -> {
            val progress = (uploadStatus as UploadStatus.Progress).percentage
            Dialog(onDismissRequest = {}) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(progress = progress / 100f, modifier = Modifier.size(80.dp))
                    Text(text = "$progress%", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold,)
                }
            }
        }
        is UploadStatus.Success -> {
            // Show success UI
            context.showToast("Upload successful")
            navigator(BaseDestination.Up().addResult(KeyUpdated, true))
        }
        is UploadStatus.Error -> {
            // Show error UI
            Timber.e("Upload ${(uploadStatus as UploadStatus.Error).exception}")
            context.showToast("Upload failed ${(uploadStatus as UploadStatus.Error).exception}")
            navigator(BaseDestination.Up().addResult(KeyUpdated, true))
        }
        else -> {
            // Initial or idle state UI
        }
    }

    RecordingScreen(
        false,
        onRecordingFinished = {
            it?.let {
                viewModel.uploadVideo(it)
            }
        }
    )
}

@Composable
fun RecordingScreen(
    isLoading: IsLoading,
    onRecordingFinished: (File?) -> Unit,
    viewModel: RecordViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.VIDEO_CAPTURE)
        }
    }
    val uiState by viewModel.uiState.collectAsState()
    val recordingState = remember { mutableStateOf<Recording?>(null) }

    val durationText = remember(uiState.recordedDuration) {
        val minutes = TimeUnit.NANOSECONDS.toMinutes(uiState.recordedDuration)
        val seconds = TimeUnit.NANOSECONDS.toSeconds(uiState.recordedDuration) % 60
        String.format("%d:%02d", minutes, seconds)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CameraPreview(
                cameraController = cameraController,
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = {
                    cameraController.cameraSelector =
                        if (cameraController.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                            CameraSelector.DEFAULT_BACK_CAMERA
                        } else CameraSelector.DEFAULT_FRONT_CAMERA
                },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Camera switcher"
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.showTime) {
                    Text(
                        text = durationText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(if (uiState.isRecording) Color.Red else Color.White)
                            .clickable {
                                if (uiState.isRecording) {
                                    if (viewModel.getPause()) {
                                        viewModel.resumeRecording()
                                        recordingState.value?.resume()
                                    } else {
                                        viewModel.pauseRecording()
                                        recordingState.value?.pause()
                                    }
                                } else {
                                    viewModel.startRecording()
                                    startRecording(context, cameraController, recordingState, {
                                        viewModel.setPause(it)
                                    }, onRecordingFinished)
                                }
                            }
                    )
                    if (uiState.showStopButton) {
                        Button(
                            onClick = {
                                viewModel.stopRecording()
                                recordingState.value?.stop()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Upload",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
            if (uiState.showRec) {
                Text(
                    text = "REC",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 16.dp)
                )
            }

            if (isLoading) {
                Dialog(onDismissRequest = {}) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}


@SuppressLint("MissingPermission")
private fun startRecording(
    context: Context,
    cameraController: LifecycleCameraController,
    recording: MutableState<Recording?>,
    isPaused: (Boolean) -> Unit,
    onRecordingFinished: (File?) -> Unit
) {
    if (recording.value != null) {
        recording.value?.close()
        recording.value = null
        onRecordingFinished(null)
        return
    }
    if (!context.isPermissionGranted(CAMERA_PERMISSION)) {
        return
    }
    val fileName = "VID_${System.currentTimeMillis()}"
    val outputFile = File(context.filesDir, fileName)

    recording.value = cameraController.startRecording(
        FileOutputOptions.Builder(outputFile).build(),
        AudioConfig.create(true),
        ContextCompat.getMainExecutor(context)
    ) { event ->
        when (event) {
            is VideoRecordEvent.Finalize -> {
                if (event.hasError()) {
                    recording.value?.close()
                    recording.value = null
                    context.showToast("Video recording failed")
                } else {
                    context.showToast("Video recording succeeded")
                    onRecordingFinished(outputFile)
                }
            }
            is VideoRecordEvent.Start -> {
                context.showToast("Video recording started")
            }
            is VideoRecordEvent.Pause -> {
                isPaused(true)
                context.showToast("Video recording paused")
            }
            is VideoRecordEvent.Resume -> {
                isPaused(false)
                context.showToast("Video recording resumed")
            }
            is VideoRecordEvent.Status -> {
                println("Recorded duration ${TimeUnit.NANOSECONDS.toMinutes(event.recordingStats.recordedDurationNanos)}")
                println("Recorded bytes ${event.recordingStats.numBytesRecorded}")
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewRecordingScreen() {
    RecordingScreen(
        CameraUiState(),
        onRecordingStart = {},
        onRecordingStop = {},
        onRecordingFinished = {}
    )
}*/
