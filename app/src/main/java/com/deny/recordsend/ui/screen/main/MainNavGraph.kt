package com.deny.recordsend.ui.screen.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.deny.domain.models.UploadedVideoEntity
import com.deny.recordsend.extensions.getThenRemove
import com.deny.recordsend.ui.AppDestination
import com.deny.recordsend.ui.base.KeyUpdated
import com.deny.recordsend.ui.composable
import com.deny.recordsend.ui.navigate
import com.deny.recordsend.ui.screen.main.dashboard.DashboardScreen
import com.deny.recordsend.ui.screen.main.list.ListDataScreen
import com.deny.recordsend.ui.screen.main.record.RecordScreen
import com.deny.recordsend.ui.screen.main.videoplayer.VideoPlayerScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
) {
    navigation(
        route = AppDestination.MainNavGraph.route,
        startDestination = MainDestination.Home.destination
    ) {

        composable(destination = MainDestination.Home) { backStackEntry ->
            val isUpdated = backStackEntry.savedStateHandle
                .getThenRemove<Boolean>(KeyUpdated) ?: false
            DashboardScreen(
                navigator = { destination ->
                    navController.navigate(destination, destination.parcelableArgument)
                },
                isUpdated = isUpdated
            )
        }

        composable(destination = MainDestination.Record) { backStackEntry ->
            RecordScreen(
                navigator = { destination -> navController.navigate(destination) })
        }

        composable(destination = MainDestination.List) { backStackEntry ->
            ListDataScreen(
                navigator = { destination -> navController.navigate(destination) })
        }

        composable(destination = MainDestination.Player) { backStackEntry ->
            VideoPlayerScreen(
                navigator = { destination -> navController.navigate(destination) },
                id = backStackEntry.arguments?.getInt(KeyId) ?: 0
            )
        }

    }
}
