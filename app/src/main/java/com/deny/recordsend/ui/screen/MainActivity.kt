package com.deny.recordsend.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.deny.recordsend.extensions.isPermissionGranted
import com.deny.recordsend.ui.AppNavGraph
import com.deny.recordsend.ui.theme.ComposeTheme
import com.deny.recordsend.utilities.CAMERA_PERMISSION
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isPermissionGranted(CAMERA_PERMISSION).not()) {
            ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                0
            )
        }
        setContent {
            ComposeTheme {
                AppNavGraph(navController = rememberNavController())
            }
        }
    }
}
