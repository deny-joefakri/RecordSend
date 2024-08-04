package com.deny.data.remote.providers

import android.content.Context
import com.cloudinary.android.MediaManager

object CloudinaryServiceProvider {

    fun initCloudinary(context: Context, config: Map<String, Any>) {
        MediaManager.init(context, config)
    }

    fun getMediaManager(): MediaManager {
        return MediaManager.get()
    }
}