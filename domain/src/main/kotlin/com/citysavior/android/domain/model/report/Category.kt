package com.citysavior.android.domain.model.report


/**
 * 임시로 카테고리에 ko, en을 Domain Layer에 추가
 * 향후 Presentation Layer에서 다국어 지원을 위해 변환이 필요함
 */
enum class Category(
    val korean : String,
    val english : String,
){
    ROAD_TRAFFIC("도로 및 교통", "Road and Traffic"),
    STREET("거리", "Street"),
    BUILD_STRUCTURE("건물 및 구조물", "Building and Structure"),
    ENVIRONMENT("환경", "Environment"),
    PARK_PUBLIC("공원 및 공공 시설", "Park and Public Facility"),
    CITY_OBSTACLE("도시 장애물", "City Obstacle"),
    SCHOOL_ZONE("학교 구역", "School Zone"),
    OTHER("기타", "Other");
}