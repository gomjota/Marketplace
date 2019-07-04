package com.juangomez.domain.mappers

import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.product.Product

fun Product.toCartItem(): CartItem {
    return CartItem(product = this)
}