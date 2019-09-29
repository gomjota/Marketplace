package com.juangomez.persistence.dao

import androidx.room.*
import com.juangomez.persistence.entities.DatabaseCartEntity

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    suspend fun getCart(): List<DatabaseCartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: DatabaseCartEntity)

    @Update
    suspend fun updateCart(cart: DatabaseCartEntity)

    @Query("DELETE FROM cart")
    suspend fun deleteCart()
}