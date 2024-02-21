package com.citysavior.android.data.dto.report.response

import com.citysavior.android.domain.model.report.Comment
import com.citysavior.android.domain.model.report.ReportPoint
import com.citysavior.android.domain.model.report.ReportPointDetail
import java.time.LocalDate

data class ReportDetailResponse(
    val description : String,
    val img_url : String,
    val reportDate : LocalDate,
    val repairedDate : LocalDate?,
    val comments : List<CommentDto>,
)

data class CommentDto(
    val commentId : Long,
    val content : String,
    val createdDate: LocalDate,
)

fun ReportDetailResponse.toDomain(
    reportPoint: ReportPoint,
) = ReportPointDetail(
    id = reportPoint.id,
    point = reportPoint.point,
    category = reportPoint.category,
    weight = reportPoint.weight,
    description = description,
    imgUrl = img_url,
    reportDate = reportDate,
    repairedDate = repairedDate,
    comments = comments.toDomain(),
)

fun CommentDto.toDomain() = Comment(
    id = commentId,
    content = content,
    createdDate = createdDate,
)

fun List<CommentDto>.toDomain() = map(CommentDto::toDomain)