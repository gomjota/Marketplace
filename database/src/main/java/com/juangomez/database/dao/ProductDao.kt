package com.juangomez.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.juangomez.database.entities.DatabaseProductEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ProductDao {

    @Query("SELECT * FROM products")
    fun getAll(): Flowable<List<DatabaseProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<DatabaseProductEntity>): Completable

    @Query("DELETE FROM products")
    fun deleteAll(): Completable
}