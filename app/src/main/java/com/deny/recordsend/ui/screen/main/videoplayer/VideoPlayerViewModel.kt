package com.deny.recordsend.ui.screen.main.videoplayer

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.cloudinary.android.MediaManager
import com.cloudinary.android.cldvideoplayer.CldVideoPlayer
import com.deny.data.local.services.UploadedVideoDao
import com.deny.domain.models.UploadedVideoEntity
import com.deny.domain.repositories.LocalRepository
import com.deny.recordsend.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val mediaManager: MediaManager,
    private val repository: LocalRepository
) : BaseViewModel() {

    /*private val videoUrl = "https://res.cloudinary.com/dfzx5ovbg/video/upload/v1722723358/Tesing.mp4"
    val cldVideoPlayer: CldVideoPlayer = CldVideoPlayer(context, videoUrl)

    val player: Player
        get() = cldVideoPlayer.player*/


    private val _playerState = MutableStateFlow<Player?>(null)
    val playerState: StateFlow<Player?> = _playerState

    private val mContext by lazy { context }

    private var _cldVideoPlayer: CldVideoPlayer? = null

    val player: Player?
        get() = _cldVideoPlayer?.player

    private fun setVideoUrl(url: String) {
        Timber.e("setVideoUrl $url")
        _cldVideoPlayer = CldVideoPlayer(mContext, url)
        _cldVideoPlayer?.play()
        _playerState.value = _cldVideoPlayer?.player
    }

    suspend fun loadVideoById(videoId: Int) {
        viewModelScope.launch {
            repository.getVideo(videoId).collect { videoList ->
                setVideoUrl(videoList.secureUrl)
            }
        }

    }

    fun play(){
        _cldVideoPlayer?.play()
    }


    override fun onCleared() {
        super.onCleared()
//        cldVideoPlayer.release()
    }
}
