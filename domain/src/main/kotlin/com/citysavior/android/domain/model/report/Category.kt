package com.citysavior.android.domain.model.report

enum class Category(
    val korean : String,
){
    ROAD_TRAFFIC("도로 및 교통"),
    STREET("거리"),
    BUILD_STRUCTURE("건물 및 구조물"),
    ENVIRONMENT("환경"),
    PARK_PUBLIC("공원 및 공공 시설"),
    CITY_OBSTACLE("도시 장애물"),
    SCHOOL_ZONE("학교 구역"),
    OTHER("기타");
}