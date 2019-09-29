package com.juangomez.data.mappers

import com.juangomez.data.entities.ProductEntity
import com.juangomez.domain.models.product.Product


fun List<ProductEntity>.toModel(): List<Product> {
    return map { it.toModel() }
}

fun ProductEntity.toModel(): Product {
    return Product(code, name, price)
}

fun List<Product>.toEntity(): List<ProductEntity> {
    return map { it.toEntity() }
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(code, name, price)
}