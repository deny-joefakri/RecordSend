package com.deny.recordsend.ui.screen.main.dashboard

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.deny.domain.models.UploadStatus
import com.deny.recordsend.R
import com.deny.recordsend.extensions.collectAsEffect
import com.deny.recordsend.extensions.showToast
import com.deny.recordsend.lib.IsLoading
import com.deny.recordsend.ui.base.BaseDestination
import com.deny.recordsend.ui.base.BaseScreen
import com.deny.recordsend.ui.common.AppBar
import com.deny.recordsend.ui.showToast
import com.deny.recordsend.utilities.FILE_NAME
import kotlinx.coroutines.launch
import timber.log.Timber


@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    navigator: (destination: BaseDestination) -> Unit,
    isUpdated: Boolean = false,
) = BaseScreen(
    isDarkStatusBarIcons = false,
) {
    val context = LocalContext.current
//    viewModel.error.collectAsEffect { e -> e.showToast(context) }
    viewModel.navigator.collectAsEffect { destination -> navigator(destination) }

    val isLoading: IsLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val dashboardUIState: DashboardUIState by viewModel.dashboardState.collectAsStateWithLifecycle()
    val uploadStatus: UploadStatus by viewModel.uploadStatus.collectAsState()

    LaunchedEffect(uploadStatus) {
        when (uploadStatus) {
            is UploadStatus.Success -> {
                context.showToast("Upload successful")
            }
            is UploadStatus.Error -> {
                context.showToast("Upload failed")
            }
            else -> { /* Do nothing */ }
        }
    }

    LaunchedEffect(Unit) {
        if (isUpdated) {
//            viewModel.updateState()

        }
    }

    HomeScreenContent(
        isLoading = isLoading,
        onStartRecording = viewModel::navigateToRecording,
        onUploadVideo = {  },
        onNavigateToList = viewModel::navigateToList,
        dashboardUIState = dashboardUIState,
    )
}

@Composable
private fun HomeScreenContent(
    isLoading: IsLoading,
    onStartRecording: () -> Unit,
    onUploadVideo: () -> Unit,
    onNavigateToList: () -> Unit,
    dashboardUIState: DashboardUIState,
) {
    val context = LocalContext.current
    /*var thumbnail by remember { mutableStateOf<Bitmap?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(dashboardUIState.fileUri) {
        dashboardUIState.fileUri?.let { uri ->
            scope.launch {
                thumbnail = context.generateVideoThumbnail(uri)
            }
        }
    }*/

    Scaffold(topBar = {
        AppBar(
            R.string.app_name,
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /*Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 250.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (thumbnail != null) {
                        Image(
                            bitmap = thumbnail!!.asImageBitmap(),
                            contentDescription = "Video Preview",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black)
                        )
                    } else {
                        Text("No video recorded", color = Color.Gray)
                    }
                }*/

                /*Button(
                    onClick = onUploadVideo,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Upload Video")
                }*/

                Button(
                    onClick = onNavigateToList,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Show List Uploaded", color = Color.White)
                }

                Button(
                    onClick = onStartRecording,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Start Recording", color = Color.White)
                }
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

@Preview(showBackground = true)
@Composable
fun HomeScreenContentPreview() {
    HomeScreenContent(
        isLoading = false,
        onStartRecording = {},
        onUploadVideo = {},
        onNavigateToList = {},
        dashboardUIState = DashboardUIState(doFileExists = false, fileUri = null)
    )
}



