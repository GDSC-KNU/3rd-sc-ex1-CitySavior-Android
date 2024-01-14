package com.citysavior.android.domain.model.report

class Category(
    val id : Long,
    val name : String,
    val description : String,
){
    companion object{
        fun fixture(
            id : Long = 1L,
            name : String = "공공시설",
            description : String = "description",
        ) : Category{
            return Category(
                id = id,
                name = name,
                description = description,
            )
        }
    }
}