package com.citysavior.android.domain.model.report


/**
 * 코틀린에서 사용하기 위해 만든 위치정보를 저장하는 커스텀 클래스
 */
class Point(
    val latitude: Double,
    val longitude: Double,
){
    companion object {
        fun fixture(
            latitude: Double = 37.123456,
            longitude: Double = 127.123456,
        ): Point {
            return Point(
                latitude = latitude,
                longitude = longitude,
            )
        }
    }
}