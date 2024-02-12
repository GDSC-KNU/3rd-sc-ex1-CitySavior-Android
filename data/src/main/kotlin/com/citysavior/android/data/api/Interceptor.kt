package com.citysavior.android.data.api

import com.citysavior.android.data.api.ApiConstants.BASE_URL
import com.citysavior.android.data.dto.auth.response.TokenResponse
import com.citysavior.android.data.dto.auth.response.toDomain
import com.citysavior.android.domain.model.auth.JwtToken
import com.citysavior.android.domain.repository.auth.JwtTokenRepository
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject


class AuthInterceptor @Inject constructor(
    private val jwtTokenRepository: JwtTokenRepository
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val originRequest = response.request
        if(originRequest.header("Authorization").isNullOrEmpty()){
            return null
        }
        val expiredAccessToken = originRequest.header("Authorization")!!.split(" ")[1]

        var jwt : JwtToken?

        synchronized(this){
            runBlocking {
                jwt = jwtTokenRepository.findJwtToken()
            }
            if(jwt == null)
                return null
            if(expiredAccessToken != jwt!!.accessToken){// 토큰이 이미 재발급된 경우, 기존api 호출
                return originRequest.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer ${jwt!!.accessToken}")
                    .build()
            }

            // 토큰 재발급
            val refreshRequest = Request.Builder()
                .url("$BASE_URL/auth/token/refresh")
                .post("".toRequestBody())
                .addHeader("authorization", "Bearer ${jwt!!.refreshToken}")
                .build()
            val refreshResponse = OkHttpClient().newCall(refreshRequest).execute()
            if(refreshResponse.code == 200) {
                val gson = Gson()
                val newToken = gson.fromJson(refreshResponse.body?.string(), TokenResponse::class.java)
                runBlocking {
                    jwtTokenRepository.saveJwtToken(newToken.toDomain())
                }
                return originRequest.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer ${newToken.accessToken}")
                    .build()
            }else{
                runBlocking {
                    jwtTokenRepository.deleteJwtToken()
                }
            }
        }
        return null
    }
}

class HeaderInterceptor @Inject constructor(
    private val jwtTokenRepository: JwtTokenRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (chain.request().headers["Auth"] == "false") {
            val newRequest = chain.request().newBuilder()
                .removeHeader("Auth")
                .build()
            return chain.proceed(newRequest)
        }

        var accessToken: String
        runBlocking {
            val jwtToken = jwtTokenRepository.findJwtToken()!!
            accessToken = jwtToken.accessToken
        }
        val newRequest = chain.request().newBuilder()
            .removeHeader("Auth")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()


        return chain.proceed(newRequest)
    }
}