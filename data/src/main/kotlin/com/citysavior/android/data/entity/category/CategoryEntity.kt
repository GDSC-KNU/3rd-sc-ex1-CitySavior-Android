package com.citysavior.android.data.entity.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
class CategoryEntity (
    @PrimaryKey(autoGenerate = false)
    val id : Long,
    val name : String,
    val description : String,
)