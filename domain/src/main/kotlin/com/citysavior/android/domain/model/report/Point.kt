package com.citysavior.android.domain.model.report

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


/**
 * 코틀린에서 사용하기 위해 만든 위치정보를 저장하는 커스텀 클래스
 */
class Point(
    val latitude: Double,
    val longitude: Double,
){

    fun calculateDistance(other: Point): Double {
        val R = 6371000 // 지구의 반지름 (미터)
        val dLat = Math.toRadians(other.latitude - latitude)
        val dLon = Math.toRadians(other.longitude - longitude)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(other.latitude)) * cos(Math.toRadians(latitude)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c
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