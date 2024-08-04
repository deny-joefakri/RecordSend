package com.deny.recordsend.ui

import android.content.Context
import com.deny.domain.exceptions.ApiException
import com.deny.recordsend.R
import com.deny.recordsend.extensions.showToast

fun Throwable.userReadableMessage(context: Context): String {
    return when (this) {
        is ApiException -> error?.message
        else -> message
    } ?: context.getString(R.string.error_generic)
}

fun Throwable.showToast(context: Context) =
    context.showToast(userReadableMessage(context))
