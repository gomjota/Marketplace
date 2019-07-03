package com.juangomez.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juangomez.database.dao.ProductDao
import com.juangomez.database.entities.DatabaseProductEntity

@Database(entities = [DatabaseProductEntity::class], version = 1)
abstract class MarketplaceDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductDao
}