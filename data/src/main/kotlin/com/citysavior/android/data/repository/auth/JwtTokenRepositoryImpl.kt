package com.citysavior.android.data.repository.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.citysavior.android.domain.model.auth.JwtToken
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.common.toAsync
import com.citysavior.android.domain.repository.auth.JwtTokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JwtTokenRepositoryImpl @Inject constructor(
    private val datastore : DataStore<Preferences>
) : JwtTokenRepository {
    override suspend fun saveJwtToken(jwtToken: JwtToken): Async<Unit> {
        return try{
            datastore.edit {
                it[ACCESS_TOKEN] = jwtToken.accessToken
                it[REFRESH_TOKEN] = jwtToken.refreshToken
            }
            Async.Success(Unit)
        }catch (e: Exception){
            Async.Error(e)
        }
    }

    override suspend fun saveAccessToken(accessToken: String): Async<Unit> {
        return try{
            datastore.edit {
                it[ACCESS_TOKEN] = accessToken
            }
            Async.Success(Unit)
        }catch (e: Exception){
            Async.Error(e)
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String): Async<Unit> {
        return try{
            datastore.edit {
                it[REFRESH_TOKEN] = refreshToken
            }
            Async.Success(Unit)
        }catch (e: Exception){
            Async.Error(e)
        }
    }

    override fun getJwtToken(): Flow<Async<JwtToken>> {
        return datastore.data.map {
            val accessToken = it[ACCESS_TOKEN]
            val refreshToken = it[REFRESH_TOKEN]
            if(accessToken == null || refreshToken == null){
                throw IllegalStateException("token is not initialized")
            }
            JwtToken(
                accessToken= accessToken,
                refreshToken= refreshToken,
            )
        }.toAsync()
    }

    override suspend fun findJwtToken(): JwtToken? {
        return try {
            datastore.data.map {
                val accessToken = it[ACCESS_TOKEN]
                val refreshToken = it[REFRESH_TOKEN]
                if(accessToken == null || refreshToken == null){
                    throw IllegalStateException("token is not initialized")
                }
                JwtToken(
                    accessToken= accessToken,
                    refreshToken= refreshToken,
                )
            }.first()
        }catch (e: Exception){
            null
        }
    }

    override suspend fun deleteJwtToken(): Async<Unit> {
        return try{
            datastore.edit {
                it.remove(ACCESS_TOKEN)
                it.remove(REFRESH_TOKEN)
            }
            Async.Success(Unit)
        }catch (e: Exception){
            Async.Error(e)
        }
    }
    companion object{
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

}
