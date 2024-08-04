package com.deny.recordsend.ui.screen.main.record

data class CameraUiState(
    val isRecording: Boolean = false,
    val showRec: Boolean = false,
    val showTime: Boolean = false,
    val showStopButton: Boolean = false,
    val recordingTime: String = "00:00:00",
    val recordedDuration: Long = 0L
)