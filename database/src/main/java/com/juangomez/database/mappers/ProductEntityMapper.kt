package com.juangomez.database.mappers

import com.juangomez.data.entities.ProductEntity
import com.juangomez.database.entities.DatabaseProductEntity
import io.reactivex.Flowable
import io.reactivex.Single

fun Single<List<DatabaseProductEntity>>.toEntity() = this.map { it.toEntity() }

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