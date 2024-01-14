package com.citysavior.android.data.dto.auth.response

import com.citysavior.android.domain.model.auth.JwtToken

data class TokenResponse(
    val accessToken : String,
    val refreshToken : String,
)

fun TokenResponse.toDomain() = JwtToken(
    accessToken = accessToken,
    refreshToken = refreshToken,
)
