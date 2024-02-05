package com.citysavior.android.domain.model.report

import kotlin.math.sqrt


/**
 * 코틀린에서 사용하기 위해 만든 위치정보를 저장하는 커스텀 클래스
 */
class Point(
    val latitude: Double,
    val longitude: Double,
){

    fun calculateDistance(other: Point): Double {
        val x = longitude - other.longitude
        val y = latitude - other.latitude
        return sqrt(x * x + y * y)
    }
    companion object {
        fun fixture(
            latitude: Double = 35.893,
            longitude: Double = 128.613,
        ): Point {
            return Point(
                latitude = latitude,
                longitude = longitude,
            )
        }
    }
}