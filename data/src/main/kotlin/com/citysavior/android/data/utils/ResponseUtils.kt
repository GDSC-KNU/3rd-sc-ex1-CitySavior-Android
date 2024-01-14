package com.citysavior.android.data.utils

import com.citysavior.android.domain.model.common.Async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/**
 * Convert [Response] to [Async] type.
 *
 * Return value is [Async.Success] if response is successful and body is not null.
 *
 * Return value is [Async.Error] if response is not successful or body is null or convert error.
 *
 * Return Async is Safe to use.
 */
suspend fun <Data, Domain> invokeApiAndConvertAsync(
    api: suspend () -> Response<Data>,
    convert: suspend Data.() -> Domain
): Async<Domain> {
    return try {
        val response = api.invoke()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Async.Success((body as Data?)!!.convert())
        } else {
            Async.Error(Throwable(response.message()))
        }
    } catch (e: Exception) {//response body convert error
        Async.Error(e)
    }
}

/**
 * Convert Api response to [Flow] type.
 *
 * Return value is [Async.Success] if response is successful and body is not null.
 *
 * Throw [Throwable] if response is not successful or body is null or convert error.
 *
 * Repeat invoke api with [intervalMillis] interval.
 *
 * Error caught when [Flow.toAsync] is called.
 */
suspend fun <Data, Domain> invokeApiAndMakeFlow(
    api: suspend () -> Response<Data>,
    intervalMillis: Long,
    convert: Data.() -> Domain
): Flow<Domain> = flow {
    while (true) {
        val response = api.invoke()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            emit((body as Data?)!!.convert())
        } else {
            throw Throwable(response.message())
        }
        delay(intervalMillis)
    }
}
