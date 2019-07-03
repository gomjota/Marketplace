package com.juangomez.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class DatabaseProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val code: String,
    val name: String,
    val price: Float
)