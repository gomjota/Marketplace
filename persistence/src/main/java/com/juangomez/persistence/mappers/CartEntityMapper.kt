package com.juangomez.persistence.mappers

import com.juangomez.data.entities.CartEntity
import com.juangomez.persistence.entities.DatabaseCartEntity

fun List<DatabaseCartEntity>.toEntity(): List<CartEntity> {
    return map { it.toEntity() }
}

fun DatabaseCartEntity.toEntity(): CartEntity {
    return CartEntity(products = products.toList().toEntity())
}

fun CartEntity.toDatabaseEntity(): DatabaseCartEntity {
    return DatabaseCartEntity(products = products.toDatabaseEntity())
}