package com.citysavior.android.presentation.main.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.common.onSuccess
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.repository.report.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel(){
    val reports: StateFlow<Async<List<ReportPoint>>> get() = _reports
    private val _reports = MutableStateFlow<Async<List<ReportPoint>>>(Async.Loading)


    fun getReports(
        latitude: Double,
        longitude: Double,
    ){
        viewModelScope.launch {
            val resp = reportRepository.getReportList(latitude, longitude)
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

}