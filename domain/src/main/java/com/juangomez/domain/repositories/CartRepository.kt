package com.juangomez.domain.repositories

import com.juangomez.domain.models.cart.Cart
import io.reactivex.Completable
import io.reactivex.Flowable

interface CartRepository {

    fun getCart(): Flowable<Cart>

    fun setCart(cart: Cart): Completable

    fun deleteCart(): Completable
}