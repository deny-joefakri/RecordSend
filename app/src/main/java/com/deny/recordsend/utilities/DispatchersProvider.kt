package com.deny.recordsend.utilities

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}
