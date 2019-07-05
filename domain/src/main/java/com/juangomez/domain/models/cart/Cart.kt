package com.juangomez.domain.models.cart

import com.juangomez.domain.mappers.toCartItemModel
import com.juangomez.domain.models.product.Product

data class Cart(var items: MutableList<CartItem>) {

    val totalPrice: Float
        get() = items.sumByDouble { it.cartPrice.toDouble() }.toFloat()

    fun addProduct(product: Product) {
        items.add(product.toCartItemModel())
    }

    fun removeProduct(product: Product) {
        items.dropLastWhile { it.product.code == product.code }
    }
}