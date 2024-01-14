package com.citysavior.android.data.repository.report

import com.citysavior.android.data.api.ApiService
import com.citysavior.android.data.dto.report.request.CreateReportCommentRequest
import com.citysavior.android.data.dto.report.response.toDomain
import com.citysavior.android.data.dto.report.response.toEntity
import com.citysavior.android.data.entity.category.CategoryDatabase
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
    private val db: CategoryDatabase,
) : ReportRepository {
    override suspend fun getReportList(
        latitude: Double,
        longitude: Double
    ): Async<List<ReportPoint>> {
        return invokeApiAndConvertAsync(
            api = { apiService.getReportInfo(latitude, longitude) },
            convert = {
                it.points.map { reportPointDto ->
                    reportPointDto.toDomain(
                        db.categoryDao().getCategoryById(reportPointDto.categoryId)
                    )
                 }
            }
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
        categoryId: Long,
        damageRatio: Int
    ): Async<Long> {
        val formFile = MultipartBody.Part.createFormData("file", fileName, file.asRequestBody())
        return invokeApiAndConvertAsync(
            api = { apiService.createReport(formFile, latitude, longitude, detail, categoryId, damageRatio) },
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

    override suspend fun getCategoryList(): Async<List<Category>> {
        val resp = apiService.getCategoryList()
        return try {
            if(!resp.isSuccessful || resp.body() == null){
                return Async.Error(Exception("getCategoryList failed"))
            }
            val categoryDtoList = resp.body()!!.categories
            db.categoryDao().insertAllCategory(categoryDtoList.map { it.toEntity() })
            Async.Success(categoryDtoList.map { it.toDomain() })
        } catch (e: Exception) {
            Async.Error(e)
        }
    }
}