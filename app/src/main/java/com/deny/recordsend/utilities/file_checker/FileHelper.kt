package com.deny.recordsend.utilities.file_checker

import android.net.Uri

interface FileHelper {

    fun getFileUri(fileName: String): Uri
    fun doFileExists(fileName: String): Boolean
}