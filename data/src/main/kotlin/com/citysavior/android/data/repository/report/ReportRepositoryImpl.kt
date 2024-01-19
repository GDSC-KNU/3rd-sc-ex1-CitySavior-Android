package com.citysavior.android.data.repository.report

import com.citysavior.android.data.api.ApiService
import com.citysavior.android.data.dto.report.request.CreateReportCommentRequest
import com.citysavior.android.data.dto.report.response.toDomain
import com.citysavior.android.data.utils.invokeApiAndConvertAsync
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.model.report.ReportPointDetail
import com.citysavior.android.domain.repository.report.ReportRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : ReportRepository {
    override suspend fun getReportList(
        latitude: Double,
        longitude: Double
    ): Async<List<ReportPoint>> {
        return invokeApiAndConvertAsync(
            api = { apiService.getReportInfo(latitude, longitude) },
            convert = { it.points.toDomain() }
        )
    }

    override suspend fun getReportDetail(reportPoint: ReportPoint): Async<ReportPointDetail> {
        return invokeApiAndConvertAsync(
            api = { apiService.getReportDetail(reportPoint.id) },
            convert = { it.toDomain(reportPoint) }
        )
    }

    override suspend fun createReport(
        fileName: String,
        file: File,
        latitude: Double,
        longitude: Double,
        detail: String,
        category: Category,
        damageRatio: Int
    ): Async<Long> {
        val formFile = MultipartBody.Part.createFormData("file", fileName, file.asRequestBody())
        return invokeApiAndConvertAsync(
            api = { apiService.createReport(formFile, latitude, longitude, detail, category, damageRatio) },
            convert = { it }
        )
    }

    override suspend fun createReportComment(reportPointId: Long, content: String): Async<Long> {
        val request = CreateReportCommentRequest(content)
        return invokeApiAndConvertAsync(
            api = { apiService.createComment(reportPointId, request) },
            convert = { it }
        )
    }


}