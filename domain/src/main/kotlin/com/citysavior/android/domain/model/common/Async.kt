package com.citysavior.android.domain.model.common

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
