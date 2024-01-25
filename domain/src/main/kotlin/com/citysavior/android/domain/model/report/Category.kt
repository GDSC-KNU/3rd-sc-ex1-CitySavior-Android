package com.citysavior.android.domain.model.report

enum class Category(
    val korean : String,
){
    ROAD_TRAFFIC("도로 및 교통"),
    BUILD_STRUCTURE("건물 및 구조물"),
    ENVIRONMENT("환경"),
    PARK_PUBIC("공원 및 공공 시설"),
    FIRE_HAZARD("화재 위험"),
    SAFETY_SECURITY("안전 및 보안"),
    CITY_OBSTACLE("도시 장애물"),
    BIKE_PEDESTRIAN_PATH("자전거 및 보행자 도로"),
    STREET("거리"),
    PUBLIC_TRANSPORT("대중교통"),
    WASTE_MANAGEMENT("폐기물 관리"),
    WATER("물 관련 문제"),
    AIR_QUALITY("대기 질"),
    ANIMAL_PLANT("동식물 관련 문제"),
    HEALTH_SAFETY("건강 및 안전"),
    SCHOOL_ZONE("학교 구역"),
    OTHER("기타");
}