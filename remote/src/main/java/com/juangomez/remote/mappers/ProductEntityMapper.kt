package com.juangomez.remote.mappers

import com.juangomez.data.entities.ProductEntity
import com.juangomez.remote.entities.RemoteProductEntity
import com.juangomez.remote.responses.GetProductsResponse

fun GetProductsResponse.toEntity(): List<ProductEntity> {
    return products.map { it.toEntity() }
}

fun RemoteProductEntity.toEntity(): ProductEntity {
    return ProductEntity(code, name, price)
}