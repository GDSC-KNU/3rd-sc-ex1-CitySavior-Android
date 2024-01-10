package com.citysavior.android.data.api

import com.citysavior.android.data.api.ApiConstants.BASE_URL
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

) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val originRequest = response.request
        if(originRequest.header("Authorization").isNullOrEmpty()){
            return null
        }
        val refreshToken = runBlocking {

        }
        val refreshRequest = Request.Builder()
            .url("$BASE_URL/auth/refreshToken")
            .post("".toRequestBody())
            .addHeader("authorization", "Bearer ${refreshToken}")
            .build()
        val refreshResponse = OkHttpClient().newCall(refreshRequest).execute()
        if(refreshResponse.code == 200) {
            val gson = Gson()
            val refreshResponseJson = gson.fromJson(refreshResponse.body?.string(), Map::class.java)
            val newAccessToken = refreshResponseJson["accessToken"].toString()
            val newRefreshToken = refreshResponseJson["refreshToken"].toString()
            runBlocking {

            }
            val newRequest = originRequest.newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", "Bearer $newAccessToken")
                .build()
            return newRequest
        }else{
            runBlocking {

            }
        }
        return null

    }

}

class HeaderInterceptor @Inject constructor(

) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (chain.request().headers["Auth"] == "false") {
            val newRequest = chain.request().newBuilder()
                .removeHeader("Auth")
                .build()
            return chain.proceed(newRequest)
        }

        var token: String
        runBlocking {
            val accessToken = ""
            token = ("Bearer $accessToken")
        }
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", token)
            .build()


        return chain.proceed(newRequest)
    }
}