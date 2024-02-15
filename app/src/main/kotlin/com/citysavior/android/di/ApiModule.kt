package com.citysavior.android.di

import com.citysavior.android.data.api.ApiClient
import com.citysavior.android.data.api.ApiConstants.BASE_URL
import com.citysavior.android.data.api.AuthInterceptor
import com.citysavior.android.data.api.HeaderInterceptor
import com.citysavior.android.domain.repository.auth.JwtTokenRepository
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideHeaderInterceptor(jwtTokenRepository: JwtTokenRepository): HeaderInterceptor {
        return HeaderInterceptor(jwtTokenRepository)
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(jwtTokenRepository: JwtTokenRepository): Authenticator {
        return AuthInterceptor(jwtTokenRepository)
    }

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(headerInterceptor: HeaderInterceptor, authInterceptor: AuthInterceptor): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(headerInterceptor)
            .authenticator(authInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory{
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, object : JsonSerializer<LocalDate> {
                override fun serialize(
                    src: LocalDate?,
                    typeOfSrc: java.lang.reflect.Type?,
                    context: com.google.gson.JsonSerializationContext?
                ): com.google.gson.JsonElement {
                    return com.google.gson.JsonPrimitive(src.toString())
                }
            })
            .registerTypeAdapter(LocalDate::class.java, object : JsonDeserializer<LocalDate> {
                override fun deserialize(
                    json: com.google.gson.JsonElement?,
                    typeOfT: java.lang.reflect.Type?,
                    context: com.google.gson.JsonDeserializationContext?
                ): LocalDate {
                    return LocalDate.parse(json!!.asString)
                }
            })
            .create()
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun providerApi(retrofit: Retrofit): ApiClient {
        return FakeApiClient()
        //return retrofit.create(ApiClient::class.java)
    }
}