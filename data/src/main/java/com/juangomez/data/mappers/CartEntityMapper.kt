package com.juangomez.data.mappers

import com.juangomez.data.entities.CartEntity
import com.juangomez.data.entities.ProductEntity
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.product.Product

fun CartItem.toEntity(): ProductEntity {
    return ProductEntity(product.code, product.name, product.price)
}

fun MutableList<CartItem>.toEntity(): List<ProductEntity> {
    return map { it.toEntity() }
}

fun Cart.toEntity(): CartEntity {
    return CartEntity(items.toEntity())
}

fun ProductEntity.toCartItem(): CartItem {
    return CartItem(product = Product(code, name, price))
}

fun List<ProductEntity>.toCartsItem(): MutableList<CartItem> {
    return map { it.toCartItem() }.toMutableList()
}

fun CartEntity.toModel(): Cart {
    return Cart(products.toCartsItem())
}