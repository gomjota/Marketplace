package com.juangomez.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juangomez.persistence.entities.DatabaseProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    suspend fun getAll(): List<DatabaseProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<DatabaseProductEntity>)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}