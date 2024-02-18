package com.citysavior.android.domain.repository.report

import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.model.report.ReportPointDetail
import com.citysavior.android.domain.model.report.ReportStatistics
import com.citysavior.android.domain.params.report.CreateReportParams

interface ReportRepository {
    suspend fun getReportStatistics(point: Point): Async<ReportStatistics>
    suspend fun getReportList(
        latitude: Double,
        longitude: Double,
        radius: Int,
    ): Async<List<ReportPoint>>
    suspend fun getReportDetail(reportPoint: ReportPoint): Async<ReportPointDetail>
    suspend fun createReport(
        params: CreateReportParams,
    ): Async<Long>
    suspend fun createReportComment(reportPointId : Long, content : String) : Async<Long>

    suspend fun endReport(reportPointId: Long): Async<Unit>
}