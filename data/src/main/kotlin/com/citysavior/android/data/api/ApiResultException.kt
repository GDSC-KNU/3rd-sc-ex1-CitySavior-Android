package com.citysavior.android.data.api


/**
 * [Exception] for Api result.
 * Because of IllegalArgument request.
 */
class ApiResultException(message: String) : Exception(message)

/**
 * [Exception] for Server error.
 * IllegalState response body of JSON.
 */
class ServerErrorException(message: String) : Exception(message)

/**
 * Error response from Backend server.
 */
data class ErrorResponse(
    val message: String,
){
    fun toApiResultException() = ApiResultException(message)
}