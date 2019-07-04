package com.juangomez.domain.models.cart

import com.juangomez.domain.models.product.Product

data class CartItem(val product: Product, var cartPrice: Float = product.price)