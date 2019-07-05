package com.juangomez.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.juangomez.database.converters.ProductListConverter

@Entity(tableName = "cart")
data class DatabaseCartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var products: List<DatabaseProductEntity>
)