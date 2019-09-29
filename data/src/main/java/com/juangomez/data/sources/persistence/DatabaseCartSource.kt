package com.juangomez.data.sources.persistence

import com.juangomez.data.entities.CartEntity

interface DatabaseCartSource {

    suspend fun getCart(): CartEntity

    suspend fun insertCart(cart: CartEntity)

    suspend fun deleteCart()
}