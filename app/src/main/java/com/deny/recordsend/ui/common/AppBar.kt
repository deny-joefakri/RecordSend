package com.deny.recordsend.ui.common

import androidx.annotation.StringRes
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.deny.recordsend.R
import com.deny.recordsend.ui.theme.AppTheme.colors
import com.deny.recordsend.ui.theme.ComposeTheme

@Composable
fun AppBar(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier,
        title = { Text(
            text = stringResource(title),
            color = Color.White
        ) },
        backgroundColor = colors.topAppBarBackground, // Replace with your desired color

    )
}

@Preview(showBackground = true)
@Composable
private fun AppBarPreview() {
    ComposeTheme { AppBar(R.string.app_name) }
}
