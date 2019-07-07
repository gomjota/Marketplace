package com.juangomez.domain.models.cart

import com.juangomez.domain.mappers.toCartItemModel
import com.juangomez.domain.models.product.Product

data class Cart(var items: MutableList<CartItem>) {

    val totalPrice: Float
        get() = items.sumByDouble { it.cartPrice.toDouble() }.toFloat()

    fun addProduct(product: Product): Cart {
        items.add(product.toCartItemModel())
        return this
    }

    fun removeProduct(product: Product): Cart {
        this.items = items.apply { remove(product.toCartItemModel()) }
        return this
    }
}