package com.citysavior.android.domain.model.report

import java.time.LocalDate

open class ReportPoint(
    val id: Long,
    val point: Point,
    val category: Category,
    val weight: Int,
) {
    companion object {
        fun fixture(
            id: Long = 1L,
            point: Point = Point.fixture(),
            category: Category = Category.OTHER,
            weight: Int = 1,
        ): ReportPoint {
            return ReportPoint(
                id = id,
                point = point,
                category = category,
                weight = weight,
            )
        }
    }
}

class ReportPointDetail(
    id: Long,
    point: Point,
    category: Category,
    weight: Int,
    val description: String,
    val imgUrl: String,
    val reportDate: LocalDate,
    val repairedDate: LocalDate?,
    val comments: List<Comment>,
) : ReportPoint(
    id = id,
    point = point,
    category = category,
    weight = weight,
) {
    fun copy(
        id: Long = this.id,
        point: Point = this.point,
        category: Category = this.category,
        weight: Int = this.weight,
        description: String = this.description,
        imgUrl: String = this.imgUrl,
        reportDate: LocalDate = this.reportDate,
        repairedDate: LocalDate? = this.repairedDate,
        comments: List<Comment> = this.comments,
    ): ReportPointDetail {
        return ReportPointDetail(
            id = id,
            point = point,
            category = category,
            weight = weight,
            description = description,
            imgUrl = imgUrl,
            reportDate = reportDate,
            repairedDate = repairedDate,
            comments = comments,
        )
    }

    companion object {
        fun fixture(
            id: Long = 1L,
            point: Point = Point.fixture(),
            category: Category = Category.OTHER,
            weight: Int = 1,
            description: String = "도로가 파여있어요.",
            imgUrl: String = "https://picsum.photos/200/300",
            reportDate: LocalDate = LocalDate.of(2024, 1, 14),
            repairedDate: LocalDate? = null,
            comments: List<Comment> = listOf(Comment.fixture()),
        ): ReportPointDetail {
            return ReportPointDetail(
                id = id,
                point = point,
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
    val id: Long,
    val content: String,
    val createdDate: LocalDate,
) {
    companion object {
        fun fixture(
            id: Long = 1L,
            content: String = "comment",
            createdDate: LocalDate = LocalDate.now(),
        ): Comment {
            return Comment(
                id = id,
                content = content,
                createdDate = createdDate,
            )
        }
    }
}