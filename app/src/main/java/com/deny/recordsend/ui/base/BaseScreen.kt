package com.deny.recordsend.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.deny.recordsend.R
import com.deny.recordsend.utilities.setStatusBarColor

@Composable
fun BaseScreen(
    isDarkStatusBarIcons: Boolean? = null,
    content: @Composable () -> Unit,
) {
    if (isDarkStatusBarIcons != null) {
        setStatusBarColor(
            color = colorResource(id = R.color.statusBarColor),
            darkIcons = isDarkStatusBarIcons,
        )
    }

    content()
}
