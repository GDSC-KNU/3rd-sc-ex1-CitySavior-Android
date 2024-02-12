package com.citysavior.android.data.repository.report

import com.citysavior.android.data.api.ApiService
import com.citysavior.android.data.dto.report.request.CreateReportCommentRequest
import com.citysavior.android.data.dto.report.response.toDomain
import com.citysavior.android.data.utils.invokeApiAndConvertAsync
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.model.report.ReportPointDetail
import com.citysavior.android.domain.model.report.ReportStatistics
import com.citysavior.android.domain.repository.report.ReportRepository
import kotlinx.coroutines.delay
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : ReportRepository {
    override suspend fun getReportStatistics(point: Point): Async<ReportStatistics> {
        delay(400)
        return Async.Success(ReportStatistics.fixture(totalReports = point.longitude.toInt()))
//        return invokeApiAndConvertAsync(
//            api = { apiService.getReportStatistics(35.89231, 128.61804) },
//            convert = { it.toDomain() }
//        )
    }

    override suspend fun getReportList(
        latitude: Double,
        longitude: Double,
        radius: Int,
    ): Async<List<ReportPoint>> {
        val rand = Random.nextLong()
        return Async.Success(listOf(
            ReportPoint.fixture(id=rand + 1, point = Point.fixture(latitude + 0.001, longitude + 0.001)),
            ReportPoint.fixture(id=rand + 2,point = Point.fixture(latitude - 0.002, longitude + 0.002)),
            ReportPoint.fixture(id=rand + 3,point = Point.fixture(latitude + 0.003, longitude - 0.003)),
            ReportPoint.fixture(id=rand + 4,point = Point.fixture(latitude + 0.004, longitude + 0.004)),
            ReportPoint.fixture(id=rand + 5,point = Point.fixture(latitude - 0.005, longitude + 0.005)),
            ReportPoint.fixture(id=rand + 6,point = Point.fixture(latitude + 0.006, longitude - 0.006)),
            ReportPoint.fixture(id=rand + 7,point = Point.fixture(latitude + 0.007, longitude + 0.007)),
            ReportPoint.fixture(id=rand + 8,point = Point.fixture(latitude + 0.008, longitude - 0.008)),
        ))
//        return invokeApiAndConvertAsync(
//            api = { apiService.getReportInfo(latitude, longitude) },
//            convert = { it.points.toDomain() }
//        )
    }

    override suspend fun getReportDetail(reportPoint: ReportPoint): Async<ReportPointDetail> {
        delay(400)
        return Async.Success(ReportPointDetail.fixture(
            id = reportPoint.id,
            point = reportPoint.point,
            category = reportPoint.category,
        ))
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
        delay(400)
        return Async.Success(Random.nextLong())
        val request = CreateReportCommentRequest(content)
        return invokeApiAndConvertAsync(
            api = { apiService.createComment(reportPointId, request) },
            convert = { it }
        )
    }


}