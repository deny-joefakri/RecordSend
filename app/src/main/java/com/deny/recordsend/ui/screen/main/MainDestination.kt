package com.deny.recordsend.ui.screen.main

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.deny.recordsend.ui.base.BaseDestination

const val KeyId = "id"
const val KeyUrl = "url"
const val KeyModel = "model"

sealed class MainDestination {

    object Home : BaseDestination("home")
    object Record : BaseDestination("record")
    object List : BaseDestination("list")

    object Player : BaseDestination("player/{$KeyId}") {

        override val arguments = listOf(
            navArgument(KeyId) { type = NavType.IntType }
        )

        fun createRoute(id: Int) = apply {
            destination = "player/$id"
        }
    }

}
