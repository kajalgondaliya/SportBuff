package com.tecocraft.sportbuffapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tecocraft.sportbuffapp.common.VIDEO_URL

/**
 * Created by Akshay Jariwala
 */
class MainActivityViewModel: ViewModel() {

    private var _videoUrl = MutableLiveData<String>()
    val videoUrl: LiveData<String>
        get() = _videoUrl

    fun fetchVideoUrl() {
        _videoUrl.value = VIDEO_URL
    }
}