package com.juangomez.domain.models.cart

import com.juangomez.domain.mappers.toCartItem
import com.juangomez.domain.models.product.Product

data class Cart(var items: MutableList<CartItem>) {

    val totalPrice: Float
        get() = items.sumByDouble { it.cartPrice.toDouble() }.toFloat()

    fun addProduct(product: Product) {
        items.add(product.toCartItem())
    }

    fun removeProduct(product: Product) {
        items.dropLastWhile { it.product.code == product.code }
    }
}