package com.citysavior.android.data.dto.auth.request

data class RefreshTokenRequest(
    val accessToken: String,
    val refreshToken: String,
)
