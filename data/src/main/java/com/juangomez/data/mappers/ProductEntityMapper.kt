package com.juangomez.data.mappers

import com.juangomez.data.entities.ProductEntity
import com.juangomez.domain.models.product.Product
import io.reactivex.Single

fun Single<List<ProductEntity>>.toModel(): Single<List<Product>> {
    return this.map { it.toModel() }
}

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