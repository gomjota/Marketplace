package com.juangomez.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juangomez.data.entities.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE code = :code")
    fun getByCode(code: String): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    fun deleteAll()
}