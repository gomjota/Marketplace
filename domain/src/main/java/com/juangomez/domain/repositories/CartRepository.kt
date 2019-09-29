package com.juangomez.domain.repositories

import com.juangomez.domain.models.cart.Cart

interface CartRepository {

    suspend fun getCart(): Cart

    suspend fun setCart(cart: Cart)

    suspend fun deleteCart()
}