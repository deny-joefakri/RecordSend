package com.deny.recordsend.ui.screen.main.list

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.decode.VideoFrameDecoder
import coil.load
import com.deny.domain.models.UploadedVideoEntity
import com.deny.recordsend.R
import com.deny.recordsend.extensions.collectAsEffect
import com.deny.recordsend.ui.base.BaseDestination
import com.deny.recordsend.ui.base.BaseScreen
import com.deny.recordsend.ui.common.AppBar
import kotlinx.coroutines.launch
import timber.log.Timber


@Composable
fun ListDataScreen(
    viewModel: ListViewModel = hiltViewModel(),
    navigator: (destination: BaseDestination) -> Unit,
) = BaseScreen(
    isDarkStatusBarIcons = false,
) {
    viewModel.navigator.collectAsEffect { destination -> navigator(destination) }

    val videos: List<UploadedVideoEntity> by viewModel.videos.collectAsStateWithLifecycle()

    VideoGrid(
        videos = videos,
        onClick = viewModel::navigateToVideoPlayer
    )
}

@Composable
fun VideoGrid(videos: List<UploadedVideoEntity>, onClick: (Int) -> Unit) {
    Scaffold(topBar = {
        AppBar(
            R.string.app_name,
        )
    }) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues).background(Color.White),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(videos) { video ->
                VideoItem(video, onClick)
            }
        }

    }


}

@Composable
fun VideoItem(video: UploadedVideoEntity, onClick: (Int) -> Unit) {
    Timber.e("VideoItem: ${video.secureUrl}")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(8.dp)
            .background(Color.DarkGray)
            .clickable(onClick = {
                onClick.invoke(video.id)
            })
    ) {
        VideoThumbnail(video.secureUrl)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = video.displayName,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .background(Color.Red)
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}



@Composable
fun VideoThumbnail(videoUrl: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).components {
            add(VideoFrameDecoder.Factory())
        }.crossfade(true)
        .build()

    val painter = rememberAsyncImagePainter(
        model = videoUrl,
        imageLoader = imageLoader,
    )

    val imageState = painter.state

    if (imageState is AsyncImagePainter.State.Loading) {
        Box(
            modifier = modifier
                .clip(shape = RoundedCornerShape(12.dp))
                .background(color = Color.LightGray)
                .fillMaxWidth()
                .height(165.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.Black,
                modifier = Modifier.size(30.dp),
                strokeWidth = 2.dp
            )
        }
    }

    Image(
        painter = painter,
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .height(165.dp)
    )
}


