package com.juangomez.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class DatabaseCartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var products: List<DatabaseProductEntity>
)