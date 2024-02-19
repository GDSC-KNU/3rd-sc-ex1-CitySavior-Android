package com.citysavior.android.presentation.main.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.citysavior.android.domain.model.common.Async
import com.citysavior.android.domain.model.common.onSuccess
import com.citysavior.android.domain.model.report.Comment
import com.citysavior.android.domain.model.report.Point
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.model.report.ReportPointDetail
import com.citysavior.android.domain.model.user.UserRole
import com.citysavior.android.domain.params.report.CreateReportParams
import com.citysavior.android.domain.repository.auth.AuthRepository
import com.citysavior.android.domain.repository.report.ReportRepository
import com.citysavior.android.domain.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel(){
    val reports: StateFlow<Async<List<ReportPoint>>> get() = _reports
    private val _reports = MutableStateFlow<Async<List<ReportPoint>>>(Async.Loading)

    val userRole : StateFlow<UserRole> get() = _userRole
    private val _userRole = MutableStateFlow(UserRole.USER)

    init {
        getUserPoint()
        viewModelScope.launch {
            authRepository.getUserRole().onSuccess {
                _userRole.value = it
            }
        }
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
                    val prevReports = (_reports.value as Async.Success).data

                    val currentReports = adjustReportPointById(prevReports = prevReports, newReports = it)
                    val adjustedReportsByPoint = adjustOverlappingReportPoint(currentReports)

                    _reports.value = Async.Success(adjustedReportsByPoint)
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

    fun createReport(
        createReportParams: CreateReportParams,
    ){
        viewModelScope.launch {
            Log.d("MapViewModel", "createReport: $createReportParams")
            val userPoint = userRepository.findUserPoint()
            if(userPoint == null){
                reportRepository.createReport(createReportParams)
                getReports(createReportParams.point.latitude, createReportParams.point.longitude)
                return@launch
            }
            val copyParams = createReportParams.copy(
                point = Point(
                    latitude = userPoint.latitude,
                    longitude = userPoint.longitude,
                )
            )
            reportRepository.createReport(copyParams)
            getReports(userPoint.latitude, userPoint.longitude)
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

    fun endReport(
        reportId: Long,
    ){
        if(userRole.value != UserRole.ADMIN) return
        
        val prevReports = (_reports.value as Async.Success).data
        viewModelScope.launch {
            val resp = reportRepository.endReport(reportId)
            resp.onSuccess {
                val newReports = prevReports.filter { it.id != reportId }
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

    /**
     * [ReportPoint]의 id를 기준으로 [prevReports]와 [newReports]를 비교하여
     * [prevReports]에 없는 [newReports]를 추가하고, [prevReports]를 반환함
     */
    private fun adjustReportPointById(
        prevReports: List<ReportPoint>,
        newReports: List<ReportPoint>,
    ): List<ReportPoint> {
        val prevReportIds = prevReports.map { it.id }
        val realNewReports = newReports.filter { report -> !prevReportIds.contains(report.id) }
        return (prevReports + realNewReports).toList()
    }


    /**
     * [ReportPoint]들의 위치가 겹치는 경우, 겹치는 위치를 조정함
     * while문을 돌면서 겹치는 위치를 조정하고, 겹치는 위치가 없을 때까지 반복함
     */
    private fun adjustOverlappingReportPoint(
        reportPoints: List<ReportPoint>,
    ): List<ReportPoint>{
        val duplicatedPoints = getDuplicatedPoints(reportPoints).toMutableList()

        val adjustedPoints = (reportPoints - duplicatedPoints.toSet()).toMutableList()

        while (getDuplicatedPoints(duplicatedPoints).isNotEmpty()){
            val duplicatePoint = duplicatedPoints.first()
            val adjusted = adjustReportPoint(duplicatePoint)

            Timber.d("adjustOverlappingReportPoint: " + duplicatedPoints.size + "adjusted: " + adjusted.point.latitude + ", " + adjusted.point.longitude)
            duplicatedPoints.remove(duplicatePoint)
            adjustedPoints.add(adjusted)
        }
        //중복된 신고들이 제거되고 [duplicatedPoints]에서 남은 신고를 추가함
        adjustedPoints += duplicatedPoints
        return adjustedPoints
    }

    /**
     * 위치가 동일한 신고들을 찾아서 리스트로 반환
     * [Point]의 [latitude]와 [longitude]가 동일한 신고들을 찾아서 리스트로 반환
     */
    private fun getDuplicatedPoints(
        reportPoints: List<ReportPoint>,
    ): List<ReportPoint>{
        return reportPoints.groupBy { it.point.latitude to it.point.longitude }
            .filter { it.value.size > 1 }
            .flatMap { it.value }
    }

    /**
     * 랜덤하게 ReportPoint를 이동함
     */
    private fun adjustReportPoint(
        reportPoint: ReportPoint,
    ): ReportPoint{
        val random1 = (-1..1).random() * 0.0001
        val random2 = (-1..1).random() * 0.0001
        return reportPoint.moveCopy(Point(
            latitude = reportPoint.point.latitude + random1,
            longitude = reportPoint.point.longitude + random2,
        ))
    }


}