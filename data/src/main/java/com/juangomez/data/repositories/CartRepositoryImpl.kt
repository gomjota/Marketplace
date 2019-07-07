package com.juangomez.data.repositories

import com.juangomez.data.mappers.toEntity
import com.juangomez.data.mappers.toModel
import com.juangomez.data.sources.database.DatabaseCartSource
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.repositories.CartRepository
import io.reactivex.Completable
import io.reactivex.Flowable

class CartRepositoryImpl constructor(
    private val database: DatabaseCartSource
) : CartRepository {

    override fun getCart(): Flowable<Cart> {
        return database.getCart().map { it.toModel() }
    }

    override fun setCart(cart: Cart): Completable {
        return database.insertCart(cart.toEntity())
    }

    override fun deleteCart(): Completable {
        return database.deleteCart()
    }
}