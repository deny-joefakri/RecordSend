package com.deny.recordsend.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.ui.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

fun Context.isPermissionGranted(permissions: Array<String>) = permissions.all {
    ContextCompat.checkSelfPermission(
        this,
        it
    ) == PackageManager.PERMISSION_GRANTED
}
