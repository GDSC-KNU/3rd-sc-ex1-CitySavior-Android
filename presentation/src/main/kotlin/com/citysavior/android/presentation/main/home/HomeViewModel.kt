package com.citysavior.android.presentation.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.common.onError
import com.citysavior.android.domain.model.common.onSuccess
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.domain.model.report.ReportStatistics
import com.citysavior.android.domain.repository.report.ReportRepository
import com.citysavior.android.domain.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val reportRepository: ReportRepository,
) : ViewModel(){
    val reportStatistics :StateFlow<Async<ReportStatistics>> get() = _reportStatistics
    private val _reportStatistics = MutableStateFlow<Async<ReportStatistics>>(Async.Loading)

    init {
        getReportStatistics()
    }

    fun getReportStatistics(){
        viewModelScope.launch {
            userRepository.getUserPoint().collect{
                it.onSuccess {point ->
                    val resp = reportRepository.getReportStatistics(point)
                    _reportStatistics.value = resp
                }.onError { error ->
                    //위치정보가 없는경우 fixture로 대체
                    val resp = reportRepository.getReportStatistics(Point.fixture())
                    _reportStatistics.value = resp
                }
            }
        }
    }
}