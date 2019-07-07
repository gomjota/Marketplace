package com.juangomez.database.mappers

import com.juangomez.data.entities.CartEntity
import com.juangomez.data.entities.ProductEntity
import com.juangomez.database.entities.DatabaseCartEntity
import com.juangomez.database.entities.DatabaseProductEntity
import io.reactivex.Flowable
import io.reactivex.Single

fun Flowable<List<DatabaseCartEntity>>.toEntity(): Flowable<List<CartEntity>> = map { it.toEntity() }

fun List<DatabaseCartEntity>.toEntity(): List<CartEntity> {
    return map { it.toEntity() }
}

fun DatabaseCartEntity.toEntity(): CartEntity {
    return CartEntity(products = products.toList().toEntity())
}

fun CartEntity.toDatabaseEntity(): DatabaseCartEntity {
    return DatabaseCartEntity(products = products.toDatabaseEntity())
}