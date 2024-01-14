package com.citysavior.android.data.entity.category

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CategoryEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class CategoryDatabase : RoomDatabase(){
    abstract fun categoryDao(): CategoryDao

}