package com.deny.recordsend.ui.screen.main.record

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.deny.recordsend.extensions.isPermissionGranted
import com.deny.recordsend.ui.base.BaseDestination
import com.deny.recordsend.ui.base.BaseScreen
import com.deny.recordsend.ui.base.KeyUpdated
import com.deny.recordsend.ui.common.CameraPreview
import com.deny.recordsend.utilities.CAMERA_PERMISSION
import com.deny.recordsend.utilities.FILE_NAME
import java.io.File
import java.util.concurrent.TimeUnit

@Composable
fun RecordScreen2(
    viewModel: RecordViewModel = hiltViewModel(),
    navigator: (destination: BaseDestination) -> Unit,
) = BaseScreen(
    isDarkStatusBarIcons = false,
) {
    BackHandler {
        navigator(BaseDestination.Up().addResult(KeyUpdated, true))
    }

    RecordingScreen2(
        playRecording = {},
        onRecordingFinished = {
            navigator(BaseDestination.Up().addResult(KeyUpdated, true))
        }
    )
}

@Composable
fun RecordingScreen2(
    playRecording: () -> Unit,
    onRecordingFinished: () -> Unit
){
    val context = LocalContext.current
    val cameraController = remember {
        LifecycleCameraController(
            context
        ).apply {
            setEnabledUseCases(
                CameraController.VIDEO_CAPTURE
            )
        }
    }
    val recordingState = remember {
        mutableStateOf<Recording?>(null)
    }
    var isPaused = remember {
        false
    }
    val recordedDuration = remember { mutableStateOf(0L) }
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
            IconButton(onClick = {
                cameraController.cameraSelector =
                    if (cameraController.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    } else CameraSelector.DEFAULT_FRONT_CAMERA
            }, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Camera switcher"
                )
            }
            IconButton(onClick = {
                startRecording2(context, cameraController, recordingState, {
                    isPaused = it
                }, recordedDuration, onRecordingFinished)
            }, modifier = Modifier.align(Alignment.BottomCenter)) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Video recording"
                )
            }
            IconButton(onClick = playRecording, modifier = Modifier.align(Alignment.BottomEnd)) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Play recording"
                )
            }
            recordingState.value?.let {
                IconButton(onClick = {
                    if(isPaused){
                        it.resume()
                    } else {
                        it.pause()
                    }
                }, modifier = Modifier.align(Alignment.BottomStart)) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Pause recording"
                    )
                }
            }
            val durationText = remember(recordedDuration.value) {
                val minutes = TimeUnit.NANOSECONDS.toMinutes(recordedDuration.value)
                val seconds = TimeUnit.NANOSECONDS.toSeconds(recordedDuration.value) % 60
                String.format("%d:%02d", minutes, seconds)
            }
            Text(
                text = "Recorded Duration: ${durationText}",
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@SuppressLint("MissingPermission")
private fun startRecording2(
    context: Context,
    cameraController: LifecycleCameraController,
    recording: MutableState<Recording?>,
    isPaused: (Boolean) -> Unit,
    recordedDuration: MutableState<Long>,
    onRecordingFinished: () -> Unit
) {
    if (recording.value != null){
        recording.value?.close()
        recording.value = null
        recordedDuration.value = 0L
        onRecordingFinished()
        return
    }
    if(!context.isPermissionGranted(CAMERA_PERMISSION)){
        return
    }
    recording.value = cameraController.startRecording(
        FileOutputOptions.Builder(File(context.filesDir, FILE_NAME)).build(),
        AudioConfig.create(true),
        ContextCompat.getMainExecutor(context)
    ) { event ->
        when(event){
            is VideoRecordEvent.Finalize -> {
                if(event.hasError()){
                    recording.value?.close()
                    recording.value = null
                    Toast.makeText(context, "Video recording failed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Video recording succeeded", Toast.LENGTH_SHORT).show()
                    onRecordingFinished()
                }
            }
            is VideoRecordEvent.Start -> {
                Toast.makeText(context, "Video recording started", Toast.LENGTH_SHORT).show()
            }
            is VideoRecordEvent.Pause -> {
                isPaused(true)
                Toast.makeText(context, "Video recording paused", Toast.LENGTH_SHORT).show()
            }
            is VideoRecordEvent.Resume -> {
                isPaused(false)
                Toast.makeText(context, "Video recording resumed", Toast.LENGTH_SHORT).show()

            }
            is VideoRecordEvent.Status -> {
                recordedDuration.value = event.recordingStats.recordedDurationNanos // Update the recorded duration
                val minutes = TimeUnit.NANOSECONDS.toMinutes(recordedDuration.value)
                val seconds = TimeUnit.NANOSECONDS.toSeconds(recordedDuration.value) % 60
                println("Recorded duration ${TimeUnit.NANOSECONDS.toMinutes(event.recordingStats.recordedDurationNanos)}")
                println("Recorded bytes ${event.recordingStats.numBytesRecorded}")
            }
        }
    }
}


@Composable
fun CameraUI() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        // Top Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "REC",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Switch Camera",
                tint = Color.White
            )
        }

        // Center Focus Indicator
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(100.dp)
                .border(2.dp, Color.White)
        )

        // Bottom Section
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "00:21:10",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Switch Camera",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCameraUI() {
    CameraUI()
}
