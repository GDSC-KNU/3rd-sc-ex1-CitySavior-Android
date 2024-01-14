package com.citysavior.android.data.entity.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategory(categoryEntityList: List<CategoryEntity>)

    @Query("SELECT * FROM category")
    suspend fun getAllCategory(): List<CategoryEntity>

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getCategoryById(id: Long): CategoryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categoryEntity: CategoryEntity)

    @Query("DELETE FROM category")
    suspend fun deleteAll()
}