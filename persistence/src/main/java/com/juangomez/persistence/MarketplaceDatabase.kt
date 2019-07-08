package com.juangomez.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.juangomez.persistence.converters.ProductListConverter
import com.juangomez.persistence.dao.CartDao
import com.juangomez.persistence.dao.ProductDao
import com.juangomez.persistence.entities.DatabaseCartEntity
import com.juangomez.persistence.entities.DatabaseProductEntity

@Database(entities = [DatabaseProductEntity::class, DatabaseCartEntity::class], version = 1)
@TypeConverters(ProductListConverter::class)
abstract class MarketplaceDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductDao
    abstract fun cartDao(): CartDao
}