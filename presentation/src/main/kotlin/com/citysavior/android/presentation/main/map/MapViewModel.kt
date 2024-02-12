package com.citysavior.android.presentation.main.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.common.onSuccess
import com.citysavior.android.domain.model.report.Comment
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.model.report.ReportPointDetail
import com.citysavior.android.domain.repository.report.ReportRepository
import com.citysavior.android.domain.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
) : ViewModel(){
    val reports: StateFlow<Async<List<ReportPoint>>> get() = _reports
    private val _reports = MutableStateFlow<Async<List<ReportPoint>>>(Async.Loading)

    init {
        getUserPoint()
    }

    fun getReports(
        latitude: Double,
        longitude: Double,
        radius: Int = 1000,
    ){
        viewModelScope.launch {
            val resp = reportRepository.getReportList(latitude, longitude, radius)
            resp.onSuccess {
                if(_reports.value is Async.Loading){
                    _reports.value = Async.Success(it)
                    return@onSuccess
                }else if(_reports.value is Async.Success){
                    val prevReports = (_reports.value as Async.Success).data.toSet()
                    _reports.value = Async.Success((prevReports + it.toSet()).toList())
                    return@onSuccess
                }
            }
        }
    }

    fun getDetailReport(
        id: Long,
    ){
        if(_reports.value !is Async.Success) return
        val prevReports = (_reports.value as Async.Success).data
        val reportPoint = prevReports.find { it.id == id } ?: return

        viewModelScope.launch {
            val resp = reportRepository.getReportDetail(reportPoint)
            resp.onSuccess {detail ->
                val newReports = prevReports.map { prev -> if (prev.id == id) detail else prev }
                _reports.value = Async.Success(newReports)
            }
        }
    }

    fun createComment(
        reportId : Long,
        content : String,
    ){
        val prevReports = (_reports.value as Async.Success).data

        viewModelScope.launch {
            val resp = reportRepository.createReportComment(reportId, content)
            resp.onSuccess { newCommentId ->
                val comment = Comment.fixture(newCommentId, content)//서버에서 id 받아오고 LocalDate.now()
                val newReports = prevReports.map { prev ->
                    if(prev.id == reportId){
                        val detail = prev as ReportPointDetail
                        detail.copy(comments = detail.comments + comment)
                    }else{
                        prev
                    }
                }
                _reports.value = Async.Success(newReports)
            }
        }
    }

    fun saveUserPoint(latitude: Double, longitude: Double){
        viewModelScope.launch {
            userRepository.saveUserPoint(Point(latitude, longitude))
        }
    }


    fun getUserPoint() : Flow<Async<Point>> {
        return userRepository.getUserPoint()
    }

}