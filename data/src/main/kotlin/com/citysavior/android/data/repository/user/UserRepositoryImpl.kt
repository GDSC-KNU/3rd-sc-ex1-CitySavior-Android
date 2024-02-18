package com.citysavior.android.data.repository.user

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import com.citysavior.android.data.api.ApiClient
import com.citysavior.android.data.dto.user.response.toDomain
import com.citysavior.android.data.utils.invokeApiAndConvertAsync
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.common.toAsync
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.domain.model.user.UserInfo
import com.citysavior.android.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val datastore : DataStore<Preferences>
) : UserRepository {
    override suspend fun getUserInfo(): Async<UserInfo> {
        return invokeApiAndConvertAsync(
            api = { apiClient.getUserInfo() },
            convert = {
                Log.d("getUserInfo", "getUserInfo: ${it.toString()}")
                it.toDomain()
            }
        )
    }

    override suspend fun saveUserPoint(point: Point): Async<Unit> {
        return try{
            datastore.edit {
                it[LATITUDE] = point.latitude
                it[LONGITUDE] = point.longitude
            }
            Async.Success(Unit)
        }catch (e: Exception){
            Async.Error(e)
        }
    }

    override fun getUserPoint(): Flow<Async<Point>> {
        return datastore.data.map {
            val latitude = it[LATITUDE]
            val longitude = it[LONGITUDE]
            if(latitude == null || longitude == null){
                throw IllegalStateException("location information is not initialized")
            }
            Point(latitude = latitude, longitude = longitude)
        }.toAsync()
    }

    companion object{
        val LATITUDE = doublePreferencesKey("latitude")
        val LONGITUDE = doublePreferencesKey("longitude")
    }

}