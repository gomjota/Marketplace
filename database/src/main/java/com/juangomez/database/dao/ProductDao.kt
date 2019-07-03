package com.juangomez.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juangomez.database.entities.DatabaseProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAll(): List<DatabaseProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<DatabaseProductEntity>)

    @Query("DELETE FROM products")
    fun deleteAll()
}