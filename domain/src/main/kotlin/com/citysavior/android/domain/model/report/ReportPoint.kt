package com.citysavior.android.domain.model.report

import java.time.LocalDate

open class ReportPoint(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val category: Category,
    val weight : Int,
){
companion object{
        fun fixture(
            id: Long = 1L,
            latitude: Double = 37.123456,
            longitude: Double = 127.123456,
            category: Category = Category.fixture(),
            weight : Int = 1,
        ) : ReportPoint{
            return ReportPoint(
                id = id,
                latitude = latitude,
                longitude = longitude,
                category = category,
                weight = weight,
            )
        }
    }
}

class ReportPointDetail(
    id: Long,
    latitude: Double,
    longitude: Double,
    category: Category,
    weight: Int,
    val description : String,
    val imgUrl : String,
    val reportDate : LocalDate,
    val repairedDate : LocalDate?,
    val comments : List<Comment>,
) : ReportPoint(
    id = id,
    latitude = latitude,
    longitude = longitude,
    category = category,
    weight = weight,
){
    companion object{
        fun fixture(
            id: Long = 1L,
            latitude: Double = 35.893314,
            longitude: Double = 128.613390,
            category: Category = Category.fixture(),
            weight : Int = 1,
            description : String = "도로가 파여있어요.",
            imgUrl : String = "https://picsum.photos/200/300",
            reportDate : LocalDate = LocalDate.of(2024,1,14),
            repairedDate : LocalDate? = null,
            comments : List<Comment> = listOf(Comment.fixture()),
        ) : ReportPointDetail{
            return ReportPointDetail(
                id = id,
                latitude = latitude,
                longitude = longitude,
                category = category,
                weight = weight,
                description = description,
                imgUrl = imgUrl,
                reportDate = reportDate,
                repairedDate = repairedDate,
                comments = comments,
            )
        }
    }
}

class Comment(
    val id : Long,
    val content : String,
    val createdDate: LocalDate,
){
    companion object{
        fun fixture(
            id : Long = 1L,
            content : String = "comment",
            createdDate: LocalDate = LocalDate.now(),
        ) : Comment{
            return Comment(
                id = id,
                content = content,
                createdDate = createdDate,
            )
        }
    }
}