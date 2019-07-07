package com.juangomez.presentation.mappers

import com.juangomez.domain.models.product.Product
import com.juangomez.presentation.models.ProductPresentationModel

fun List<Product>.toPresentationModel(): List<ProductPresentationModel> {
    return map { it.toPresentationModel() }
}

fun Product.toPresentationModel(): ProductPresentationModel {
    return ProductPresentationModel(code, name, price.toString())
}