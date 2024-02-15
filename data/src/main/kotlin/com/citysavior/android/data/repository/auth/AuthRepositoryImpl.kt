package com.citysavior.android.data.repository.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.citysavior.android.data.api.ApiClient
import com.citysavior.android.data.dto.auth.request.LoginRequestV1
import com.citysavior.android.data.dto.auth.request.SignupRequestV1
import com.citysavior.android.data.utils.invokeApiAndConvertAsync
import com.citysavior.android.domain.model.auth.JwtToken
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.user.UserRole
import com.citysavior.android.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val dataStore: DataStore<Preferences>,
) : AuthRepository {
    override suspend fun login(): Async<JwtToken> {
        val uuid = getUUID()
        val request = LoginRequestV1(uuid = uuid)
        return Async.Success(JwtToken.fixture())
//        return invokeApiAndConvertAsync(
//            api = { apiService.login(request) },
//            convert = { it.toDomain() }
//        )
    }

    override suspend fun signUp(): Async<JwtToken> {
        val uuid = getUUID()
        val request = SignupRequestV1(uuid = uuid)
        return Async.Success(JwtToken.fixture())
//        return invokeApiAndConvertAsync(
//            api = { apiService.signup(request) },
//            convert = { it.toDomain() }
//        )
    }

    override suspend fun getAfterOnBoarding(): Async<Boolean> {
        return try{
            val resp =  dataStore.data.map {
                it[AFTER_ONBOARDING]
            }.first()
            Async.Success(resp ?: false)
        }catch (e: Exception){
            Async.Error(e)
        }
    }

    override suspend fun setAfterOnBoarding(): Async<Unit> {
        try {
            dataStore.edit {
                it[AFTER_ONBOARDING] = true
            }
        }catch (e: Exception){
            return Async.Error(e)
        }
        return Async.Success(Unit)
    }

    override suspend fun getUserRole(): Async<UserRole> {
        return invokeApiAndConvertAsync(
            api = { apiClient.getUserRole() },
            convert = { it }
        )
    }

    override suspend fun changeUserRole(): Async<Unit> {
        return invokeApiAndConvertAsync(
            api = { apiClient.changeUserRole() },
            convert = {  }
        )
    }


    /**
     * UUID를 가져오거나 없으면 생성해서 저장하고 가져온다.
     */
    private suspend fun getUUID(): String {
        val resp =  dataStore.data.map {
            it[UUID]
        }.first()

        return if(resp == null){
            val uuid = java.util.UUID.randomUUID().toString()
            dataStore.edit {
                it[UUID] = uuid
            }
            uuid
        }else {
            resp
        }
    }

    companion object {
        val UUID = stringPreferencesKey("uuid")
        val AFTER_ONBOARDING = booleanPreferencesKey("after_onboarding")
    }
}