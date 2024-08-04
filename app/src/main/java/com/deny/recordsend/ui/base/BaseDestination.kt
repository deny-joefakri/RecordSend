package com.deny.recordsend.ui.base

import androidx.navigation.NamedNavArgument

const val KeyUpdated = "keyUpdated"

abstract class BaseDestination(val route: String = "") {

    open val arguments: List<NamedNavArgument> = emptyList()

    open var destination: String = route

    open var parcelableArgument: Pair<String, Any?> = "" to null

    data class Up(val results: HashMap<String, Any> = hashMapOf()) : BaseDestination() {

        fun addResult(key: String, value: Any) = apply {
            results[key] = value
        }
    }
}
