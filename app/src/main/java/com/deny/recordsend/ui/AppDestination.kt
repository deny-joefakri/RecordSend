package com.deny.recordsend.ui

import com.deny.recordsend.ui.base.BaseDestination

sealed class AppDestination {

    object RootNavGraph : BaseDestination("rootNavGraph")

    object MainNavGraph : BaseDestination("mainNavGraph")
}
