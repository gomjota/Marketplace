package com.juangomez.persistence.dao

import androidx.room.*
import com.juangomez.persistence.entities.DatabaseCartEntity
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface CartDao {

    @Query("SELECT * FROM cart")
    fun getCart(): Flowable<List<DatabaseCartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCart(cart: DatabaseCartEntity): Completable

    @Update
    fun updateCart(cart: DatabaseCartEntity): Completable

    @Query("DELETE FROM cart")
    fun deleteCart(): Completable
}