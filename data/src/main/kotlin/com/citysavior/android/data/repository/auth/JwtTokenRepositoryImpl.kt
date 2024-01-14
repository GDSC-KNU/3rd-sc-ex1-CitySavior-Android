package com.citysavior.android.data.repository.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.citysavior.android.domain.model.auth.JwtToken
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.repository.auth.JwtTokenRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JwtTokenRepositoryImpl @Inject constructor(
    private val datastore : DataStore<Preferences>
) : JwtTokenRepository {
    override suspend fun saveJwtToken(jwtToken: JwtToken): Async<Unit> {
        datastore.edit {
            it[ACCESS_TOKEN] = jwtToken.accessToken
            it[REFRESH_TOKEN] = jwtToken.refreshToken
        }
        return Async.Success(Unit)
    }

    override suspend fun saveAccessToken(accessToken: String): Async<Unit> {
        datastore.edit {
            it[ACCESS_TOKEN] = accessToken
        }
        return Async.Success(Unit)
    }

    override suspend fun saveRefreshToken(refreshToken: String): Async<Unit> {
        datastore.edit {
            it[REFRESH_TOKEN] = refreshToken
        }
        return Async.Success(Unit)
    }

    override suspend fun getJwtToken(): Async<JwtToken> {
        return datastore.data.map {
            val accessToken = it[ACCESS_TOKEN]
            val refreshToken = it[REFRESH_TOKEN]
            if(accessToken == null || refreshToken == null){
                return@map Async.Error(Exception("Token is null"))
            }
            Async.Success(
                JwtToken(
                    accessToken= accessToken,
                    refreshToken= refreshToken,
                )
            )
        }.catch {
            Async.Error(it)
        }.first()
    }

    override suspend fun deleteJwtToken(): Async<Unit> {
        datastore.edit {
            it.remove(ACCESS_TOKEN)
            it.remove(REFRESH_TOKEN)
        }
        return Async.Success(Unit)
    }
    companion object{
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

}
