package com.citysavior.android.data.dto.report.response

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
