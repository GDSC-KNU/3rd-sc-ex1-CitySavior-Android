package com.citysavior.android.data.repository.report

import android.util.Log
import com.citysavior.android.data.api.ApiClient
import com.citysavior.android.data.dto.report.request.CreateReportCommentRequest
import com.citysavior.android.data.dto.report.request.toData
import com.citysavior.android.data.dto.report.response.toDomain
import com.citysavior.android.data.utils.compressImageIfNeeded
import com.citysavior.android.data.utils.invokeApiAndConvertAsync
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.model.report.ReportPointDetail
import com.citysavior.android.domain.model.report.ReportStatistics
import com.citysavior.android.domain.params.report.CreateReportParams
import com.citysavior.android.domain.repository.report.ReportRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
) : ReportRepository {
    override suspend fun getReportStatistics(point: Point): Async<ReportStatistics> {
        return invokeApiAndConvertAsync(
            api = { apiClient.getReportStatistics(point.latitude,point.longitude,1000) },
            convert = { it.toDomain() }
        )
    }

    override suspend fun getReportList(
        latitude: Double,
        longitude: Double,
        radius: Int,
    ): Async<List<ReportPoint>> {
        return invokeApiAndConvertAsync(
            api = { apiClient.getReportInfo(latitude, longitude, radius) },
            convert = { it.points.toDomain() }
        )
    }

    override suspend fun getReportDetail(reportPoint: ReportPoint): Async<ReportPointDetail> {
        return invokeApiAndConvertAsync(
            api = { apiClient.getReportDetail(reportPoint.id) },
            convert = { it.toDomain(reportPoint) }
        )
    }

    override suspend fun createReport(
        params: CreateReportParams,
    ): Async<Long> {
        val file = compressImageIfNeeded(params.file)

        val requestFile = file.asRequestBody("image/*".toMediaType()) // 파일의 MIME 타입을 지정해야 합니다.
        val requestDto = params.toData()
        Log.d("FileName", file.name)
        val formFile = MultipartBody.Part.createFormData("imgFiles", file.name, requestFile)
        val dto = Gson().toJson(requestDto).toRequestBody("application/json".toMediaType())

        return invokeApiAndConvertAsync(
            api = { apiClient.createReport(formFile,dto) },
            convert = { it }
        )
    }

    override suspend fun createReportComment(reportPointId: Long, content: String): Async<Long> {
        val request = CreateReportCommentRequest(content)
        return invokeApiAndConvertAsync(
            api = { apiClient.createComment(reportPointId, request) },
            convert = { it }
        )
    }

    override suspend fun endReport(reportPointId: Long): Async<Unit> {
        return invokeApiAndConvertAsync(
            api = { apiClient.endReport(reportPointId) },
            convert = { it }
        )
    }


}