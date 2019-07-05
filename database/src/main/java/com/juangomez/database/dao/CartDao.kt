package com.juangomez.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juangomez.database.entities.DatabaseCartEntity
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    fun getCart(): Flowable<DatabaseCartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCart(cart: DatabaseCartEntity): Completable

    @Query("DELETE FROM cart")
    fun deleteCart(): Completable
}