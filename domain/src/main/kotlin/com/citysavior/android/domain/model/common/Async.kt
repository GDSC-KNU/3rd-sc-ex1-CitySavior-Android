package com.citysavior.android.domain.model.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * A generic class that holds a value with its [Loading] and [Error] status.
 *
 * If use this class, you must handle error case before invoke [Success] case.
 */
sealed class Async<out T> {
    object Loading : Async<Nothing>()

    data class Error(val exception: Throwable) : Async<Nothing>()

    data class Success<out T>(val data: T) : Async<T>()
}

/**
 * Convert [Flow] to [Async] type.
 */
fun <T> Flow<T>.toAsync(): Flow<Async<T>> = map<T, Async<T>> { Async.Success(it) }
    .onStart { emit(Async.Loading) }
    .catch { e -> emit(Async.Error(e)) }

/**
 * Performs the given [action] on the encapsulated value if this instance represents [Async.Success].
 * Returns the original `Async` unchanged.
 */
inline fun <T> Async<T>.onSuccess(action: (T) -> Unit): Async<T> {
    if (this is Async.Success) action(data)
    return this
}

/**
 * Performs the given [action] on the encapsulated value if this instance represents [Async.Error].
 * Returns the original `Async` unchanged.
 */
inline fun <T> Async<T>.onError(action: (Throwable) -> Unit): Async<T> {
    if (this is Async.Error) action(exception)
    return this
}
