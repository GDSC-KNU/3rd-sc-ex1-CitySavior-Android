package com.citysavior.android.domain.repository.report

import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.Category
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.model.report.ReportPointDetail
import com.citysavior.android.domain.model.report.ReportStatistics
import java.io.File

interface ReportRepository {
    suspend fun getReportStatistics(point: Point): Async<ReportStatistics>
    suspend fun getReportList(
        latitude: Double,
        longitude: Double,
        radius: Int,
    ): Async<List<ReportPoint>>
    suspend fun getReportDetail(reportPoint: ReportPoint): Async<ReportPointDetail>
    suspend fun createReport(
        fileName : String,
        file : File,
        latitude: Double,
        longitude: Double,
        detail : String,
        category : Category,
        damageRatio : Int,
    ): Async<Long>
    suspend fun createReportComment(reportPointId : Long, content : String) : Async<Long>

}