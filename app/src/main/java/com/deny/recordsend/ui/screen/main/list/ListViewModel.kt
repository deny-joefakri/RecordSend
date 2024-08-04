package com.deny.recordsend.ui.screen.main.list

import androidx.lifecycle.viewModelScope
import com.deny.domain.models.UploadedVideoEntity
import com.deny.domain.repositories.LocalRepository
import com.deny.recordsend.ui.base.BaseViewModel
import com.deny.recordsend.ui.screen.main.MainDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: LocalRepository
) : BaseViewModel() {
    private val _videos = MutableStateFlow<List<UploadedVideoEntity>>(emptyList())
    val videos = _videos.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllVideos().collect { videoList ->
                _videos.value = videoList
            }
        }
    }

    fun navigateToVideoPlayer(id : Int) {
        launch { _navigator.emit(MainDestination.Player.createRoute(id)) }
    }

}
