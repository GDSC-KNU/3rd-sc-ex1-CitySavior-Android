package com.citysavior.android.di

import com.citysavior.android.data.api.ApiClient
import com.citysavior.android.data.dto.achievement.response.AchievementDto
import com.citysavior.android.data.dto.achievement.response.AchievementInfoResponse
import com.citysavior.android.data.dto.auth.request.LoginRequestV1
import com.citysavior.android.data.dto.auth.request.SignupRequestV1
import com.citysavior.android.data.dto.auth.response.TokenResponse
import com.citysavior.android.data.dto.report.request.CreateReportCommentRequest
import com.citysavior.android.data.dto.report.response.CommentDto
import com.citysavior.android.data.dto.report.response.ReportDetailResponse
import com.citysavior.android.data.dto.report.response.ReportPointDto
import com.citysavior.android.data.dto.report.response.ReportsByMapResponse
import com.citysavior.android.data.dto.report.response.StatisticsByMapResponse
import com.citysavior.android.data.dto.report.response.StatisticsDetailDto
import com.citysavior.android.data.dto.user.response.AchieveProgressDto
import com.citysavior.android.data.dto.user.response.UserInfoResponse
import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.model.user.UserRole
import okhttp3.MultipartBody
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FakeApiClient :ApiClient{
    override suspend fun login(request: LoginRequestV1): Response<TokenResponse> {
        return Response.success(TokenResponse("token", "refreshToken"))
    }

    override suspend fun signup(request: SignupRequestV1): Response<TokenResponse> {
        return Response.success(TokenResponse("token", "refreshToken"))
    }

    override suspend fun getUserRole(): Response<UserRole> {
        return Response.success(UserRole.ADMIN)
    }

    override suspend fun changeUserRole(): Response<Unit> {
        return Response.success(Unit)
    }

    override suspend fun getUserInfo(): Response<UserInfoResponse> {
        val response = UserInfoResponse(20,20,30, listOf(
            AchieveProgressDto(
                Category.AIR_QUALITY,
                "iconUrl",
                "name",
                "description",
                10,
                20
            ),
        ))
        return Response.success(response)
    }

    override suspend fun getAchievementInfo(): Response<AchievementInfoResponse> {
        return Response.success(AchievementInfoResponse(listOf(
            AchievementDto(
                "name",
                "description",
                "https://picsum.photos/200/300",
                Category.AIR_QUALITY,
                10
        ))))
    }

    override suspend fun getReportStatistics(
        latitude: Double,
        longitude: Double,
        radius: Int
    ): Response<StatisticsByMapResponse> {
        val randomR = Random.nextInt(1,100000)
        val details = List(3) {
            val random = Random.nextInt(1,100)
            StatisticsDetailDto(
                Category.values()[random % Category.values().size],
                100,
                random
            )
        }
        return Response.success(StatisticsByMapResponse(100000, randomR, details))
    }

    override suspend fun getReportInfo(
        latitude: Double,
        longitude: Double
    ): Response<ReportsByMapResponse> {
        val points = List(10) {
            val random1 = Random.nextInt(-100,100)
            val random2 = Random.nextInt(-100,100)
            ReportPointDto(
                Random.nextLong(),
                latitude + random1 * 0.0001,
                longitude + random2 * 0.0001,
                Category.AIR_QUALITY,
                1
            )
        }
        return Response.success(ReportsByMapResponse(points))
    }

    override suspend fun createReport(
        imageFiles: MultipartBody.Part,
        latitude: Double,
        longitude: Double,
        detail: String,
        category: Category,
        damageRatio: Int
    ): Response<Long> {
        return Response.success(Random(0).nextLong())
    }

    override suspend fun getReportDetail(reportId: Long): Response<ReportDetailResponse> {
        val imgUrl = if (reportId % 2 == 1L) "https://picsum.photos/200/300" else "https://picsum.photos/200/200"
        return Response.success(ReportDetailResponse(
            "도로가 파여있어요",
            imgUrl,
            LocalDate.now(),
            LocalDate.now(),
            listOf(
                CommentDto(
                    reportId,
                    "comment",
                    LocalDate.now()
                )
            )
        ))
    }

    override suspend fun createComment(
        reportId: Long,
        request: CreateReportCommentRequest
    ): Response<Long> {
        return Response.success(Random.nextLong())
    }

    override suspend fun endReport(reportId: Long): Response<Unit> {
        return Response.success(Unit)
    }
}