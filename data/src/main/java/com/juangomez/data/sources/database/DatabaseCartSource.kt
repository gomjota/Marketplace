package com.juangomez.data.sources.database

import com.juangomez.data.entities.CartEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface DatabaseCartSource {

    fun getCart(): Flowable<CartEntity>

    fun insertCart(cart: CartEntity): Completable

    fun updateCart(cart: CartEntity): Completable

    fun deleteCart(): Completable
}