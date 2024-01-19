package com.citysavior.android.data.dto.report.response

import com.citysavior.android.domain.model.report.Comment
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.model.report.ReportPointDetail
import java.time.LocalDate

data class ReportDetailResponse(
    val description : String,
    val imgUrl : String,
    val reportDate : LocalDate,
    val repairedDate : LocalDate?,
    val comments : List<CommentDto>,
)

data class CommentDto(
    val id : Long,
    val comment : String,
    val createdDate: LocalDate,
)

fun ReportDetailResponse.toDomain(
    reportPoint: ReportPoint,
) = ReportPointDetail(
    id = reportPoint.id,
    latitude = reportPoint.latitude,
    longitude = reportPoint.longitude,
    category = reportPoint.category,
    weight = reportPoint.weight,
    description = description,
    imgUrl = imgUrl,
    reportDate = reportDate,
    repairedDate = repairedDate,
    comments = comments.toDomain(),
)

fun CommentDto.toDomain() = Comment(
    id = id,
    content = comment,
    createdDate = createdDate,
)

fun List<CommentDto>.toDomain() = map(CommentDto::toDomain)