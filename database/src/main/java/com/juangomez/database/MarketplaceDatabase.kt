package com.juangomez.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.juangomez.database.converters.ProductListConverter
import com.juangomez.database.dao.CartDao
import com.juangomez.database.dao.ProductDao
import com.juangomez.database.entities.DatabaseCartEntity
import com.juangomez.database.entities.DatabaseProductEntity

@Database(entities = [DatabaseProductEntity::class, DatabaseCartEntity::class], version = 1)
@TypeConverters(ProductListConverter::class)
abstract class MarketplaceDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductDao
    abstract fun cartDao(): CartDao
}