package com.citysavior.android.domain.model.auth

class JwtToken(
    val accessToken: String,
    val refreshToken: String,
){
    companion object{
        fun fixture(
            accessToken: String = "accessToken",
            refreshToken: String = "refreshToken",
        ) : JwtToken{
            return JwtToken(
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        }
    }
}