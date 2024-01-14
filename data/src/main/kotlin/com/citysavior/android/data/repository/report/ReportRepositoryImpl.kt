package com.citysavior.android.data.repository.report

import com.citysavior.android.data.api.ApiService
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.model.report.ReportPointDetail
import com.citysavior.android.domain.repository.report.ReportRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReportRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ReportRepository {
    override suspend fun getReportList(
        latitude: Double,
        longitude: Double
    ): Async<List<ReportPoint>> {
        TODO("Not yet implemented")
    }

    override suspend fun getReportDetail(reportPoint: ReportPoint): Async<ReportPointDetail> {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override suspend fun createReportComment(reportPointId: Long, content: String): Async<Long> {
        TODO("Not yet implemented")
    }
}