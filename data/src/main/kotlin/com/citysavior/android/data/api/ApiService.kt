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
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //--------------------- Auth ---------------------//
    @POST("/auth/login/v1")
    @Headers("Auth: false")
    suspend fun login(@Body request: LoginRequestV1): Response<TokenResponse>

    @POST("/auth/signup/v1")
    @Headers("Auth: false")
    suspend fun signup(@Body request: SignupRequestV1): Response<TokenResponse>


    //--------------------- User ---------------------//
    @GET("/user/info")
    suspend fun getUserInfo(): Response<UserInfoResponse>


    //--------------------- Achievement ---------------------//
    @GET("/achievement")
    suspend fun getAchievementInfo(): Response<AchievementInfoResponse>


    //--------------------- Report ---------------------//
    @GET("/reports/statistics")
    suspend fun getReportStatistics(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ) : Response<StatisticsByMapResponse>

    @GET("/reports")
    suspend fun getReportInfo(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
    ) : Response<ReportsByMapResponse>

    @Multipart
    @POST("/reports/")
    suspend fun createReport(
        @Part imageFiles : MultipartBody.Part,
        @Part("latitude") latitude : Double,
        @Part("longitude") longitude : Double,
        @Part("detail") detail : String,
        @Part("categoryId") category : Category,
        @Part("damageRatio") damageRatio : Int,
    ) : Response<Long>

    @GET("/reports/{reportId}")
    suspend fun getReportDetail(@Path("reportId") reportId : Long) : Response<ReportDetailResponse>

    @POST("/reports/{reportId}/comment")
    suspend fun createComment(
        @Path("reportId") reportId : Long,
        @Body request : CreateReportCommentRequest,
    ) : Response<Long>

    @PATCH("/reports/{reportId}/end")
    suspend fun endReport(@Path("reportId") reportId : Long) : Response<Unit>

}
object ApiConstants {
    const val BASE_URL = "http://10.0.2.2:8080"
}