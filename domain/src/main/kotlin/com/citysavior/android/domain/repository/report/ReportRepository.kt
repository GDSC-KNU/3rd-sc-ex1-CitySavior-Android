package com.citysavior.android.domain.repository.report

import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.model.report.ReportPointDetail
import java.io.File

interface ReportRepository {
    suspend fun getReportList(
        latitude: Double,
        longitude: Double,
    ): Async<List<ReportPoint>>
    suspend fun getReportDetail(reportPoint: ReportPoint): Async<ReportPointDetail>
    suspend fun createReport(
        fileName : String,
        file : File,
        latitude: Double,
        longitude: Double,
        detail : String,
        categoryId : Long,
        damageRatio : Int,
    ): Async<Long>
    suspend fun createReportComment(reportPointId : Long, content : String) : Async<Long>

}