package com.deny.data.extensions

import com.deny.data.remote.models.response.ErrorResponse
import com.deny.data.remote.models.response.toModel
import com.deny.data.remote.providers.MoshiBuilderProvider
import com.deny.domain.exceptions.ApiException
import com.deny.domain.exceptions.NoConnectivityException
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.io.InterruptedIOException
import java.net.UnknownHostException
import kotlin.experimental.ExperimentalTypeInference

@OptIn(ExperimentalTypeInference::class)
fun <T> flowTransform(@BuilderInference block: suspend FlowCollector<T>.() -> T) = flow {
    runCatching { block() }
        .onSuccess { result -> emit(result) }
        .onFailure { exception -> throw exception.mapError() }
}

private fun Throwable.mapError(): Throwable {
    return when (this) {
        is UnknownHostException,
        is InterruptedIOException -> NoConnectivityException
        is HttpException -> {
            val errorResponse = parseErrorResponse(response())
            ApiException(
                errorResponse?.toModel(),
                code(),
                message()
            )
        }
        else -> this
    }
}

private fun parseErrorResponse(response: Response<*>?): ErrorResponse? {
    val jsonString = response?.errorBody()?.string()
    return try {
        val moshi = MoshiBuilderProvider.moshiBuilder.build()
        val adapter = moshi.adapter(ErrorResponse::class.java)
        adapter.fromJson(jsonString.orEmpty())
    } catch (exception: IOException) {
        null
    } catch (exception: JsonDataException) {
        null
    }
}
