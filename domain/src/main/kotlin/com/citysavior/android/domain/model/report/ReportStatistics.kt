package com.citysavior.android.domain.model.report

class ReportStatistics(
    val totalReports : Int,
    val resolvedReports : Int,
    val statisticsDetailList : List<StatisticsDetail>,
){
    companion object{
        fun fixture(
            totalReports : Int = 10,
            resolvedReports : Int = 5,
            statisticsDetailList : List<StatisticsDetail> = listOf(StatisticsDetail.fixture()),
        ) : ReportStatistics{
            return ReportStatistics(
                totalReports = totalReports,
                resolvedReports = resolvedReports,
                statisticsDetailList = statisticsDetailList,
            )
        }
    }
}

class StatisticsDetail(
    val category: Category,
    val totalReports : Int,
    val resolvedReports : Int,
){
    companion object{
        fun fixture(
            category: Category = Category.UNKNOWN,
            totalReports : Int = 10,
            resolvedReports : Int = 5,
        ) : StatisticsDetail{
            return StatisticsDetail(
                category = category,
                totalReports = totalReports,
                resolvedReports = resolvedReports,
            )
        }
    }
}
