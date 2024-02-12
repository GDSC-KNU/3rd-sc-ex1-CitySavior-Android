package com.citysavior.android.data.api

import com.citysavior.android.data.dto.achievement.response.AchievementInfoResponse
import com.citysavior.android.data.dto.auth.request.LoginRequestV1
import com.citysavior.android.data.dto.auth.request.SignupRequestV1
import com.citysavior.android.data.dto.auth.response.TokenResponse
import com.citysavior.android.data.dto.report.request.CreateReportCommentRequest
import com.citysavior.android.data.dto.report.response.ReportDetailResponse
import com.citysavior.android.data.dto.report.response.ReportsByMapResponse
import com.citysavior.android.data.dto.report.response.StatisticsByMapResponse
import com.citysavior.android.data.dto.user.response.UserInfoResponse
import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.model.user.UserRole
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //--------------------- Auth ---------------------//
    @POST("/v1/auth/login")
    @Headers("Auth: false")
    suspend fun login(@Body request: LoginRequestV1): Response<TokenResponse>

    @POST("/v1/auth/signup/v1")
    @Headers("Auth: false")
    suspend fun signup(@Body request: SignupRequestV1): Response<TokenResponse>

    @GET("/v1/auth/role")
    suspend fun getUserRole(): Response<UserRole>

    @PUT("/v1/auth/role")
    suspend fun changeUserRole(): Response<Unit>


    //--------------------- User ---------------------//
    @GET("/v1/user/info")
    suspend fun getUserInfo(): Response<UserInfoResponse>


    //--------------------- Achievement ---------------------//
    @GET("/v1/achievement")
    suspend fun getAchievementInfo(): Response<AchievementInfoResponse>


    //--------------------- Report ---------------------//
    @GET("/v1/reports/statistics")
    suspend fun getReportStatistics(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("radius") radius: Int,
    ) : Response<StatisticsByMapResponse>

    @GET("/v1/reports")
    suspend fun getReportInfo(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ) : Response<ReportsByMapResponse>

    @Multipart
    @POST("/v1/reports/")
    suspend fun createReport(
        @Part imageFiles : MultipartBody.Part,
        @Part("latitude") latitude : Double,
        @Part("longitude") longitude : Double,
        @Part("detail") detail : String,
        @Part("categoryId") category : Category,
        @Part("damageRatio") damageRatio : Int,
    ) : Response<Long>

    @GET("/v1/reports/{reportId}")
    suspend fun getReportDetail(@Path("reportId") reportId : Long) : Response<ReportDetailResponse>

    @POST("/v1/reports/{reportId}/comment")
    suspend fun createComment(
        @Path("reportId") reportId : Long,
        @Body request : CreateReportCommentRequest,
    ) : Response<Long>

    @PATCH("/v1/reports/{reportId}/end")
    suspend fun endReport(@Path("reportId") reportId : Long) : Response<Unit>

}
object ApiConstants {
    const val BASE_URL = "http://10.0.2.2:8080"
}