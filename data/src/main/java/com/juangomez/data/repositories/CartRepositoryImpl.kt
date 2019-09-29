package com.juangomez.data.repositories

import com.juangomez.data.mappers.toEntity
import com.juangomez.data.mappers.toModel
import com.juangomez.data.sources.persistence.DatabaseCartSource
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.repositories.CartRepository

class CartRepositoryImpl constructor(
    private val database: DatabaseCartSource
) : CartRepository {

    override suspend fun getCart(): Cart {
        return database.getCart().toModel()
    }

    override suspend fun setCart(cart: Cart) {
        return database.insertCart(cart.toEntity())
    }

    override suspend fun deleteCart() {
        return database.deleteCart()
    }
}