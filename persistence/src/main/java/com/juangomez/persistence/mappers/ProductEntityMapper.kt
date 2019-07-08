package com.juangomez.persistence.mappers

import com.juangomez.data.entities.ProductEntity
import com.juangomez.persistence.entities.DatabaseProductEntity

fun List<DatabaseProductEntity>.toEntity(): List<ProductEntity> {
    return map { it.toEntity() }
}

fun DatabaseProductEntity.toEntity(): ProductEntity {
    return ProductEntity(code, name, price)
}

fun ProductEntity.toDatabaseEntity(): DatabaseProductEntity {
    return DatabaseProductEntity(code = code, name = name, price = price)
}

fun List<ProductEntity>.toDatabaseEntity(): List<DatabaseProductEntity> {
    return this.map { it.toDatabaseEntity() }
}