package com.juangomez.persistence.sources

import com.juangomez.data.entities.CartEntity
import com.juangomez.data.sources.persistence.DatabaseCartSource
import com.juangomez.persistence.MarketplaceDatabase
import com.juangomez.persistence.dao.CartDao
import com.juangomez.persistence.mappers.toDatabaseEntity
import com.juangomez.persistence.mappers.toEntity
import io.reactivex.Completable
import io.reactivex.Flowable

class DatabaseCartSourceImpl(private val database: MarketplaceDatabase) : DatabaseCartSource {

    private val cartDao: CartDao = database.cartDao()

    override fun getCart(): Flowable<CartEntity> {
        return cartDao.getCart().map {
            if (it.isEmpty()) CartEntity(emptyList())
            else it.last().toEntity()
        }
    }

    override fun insertCart(cart: CartEntity): Completable {
        return cartDao.insertCart(cart.toDatabaseEntity())
    }

    override fun deleteCart(): Completable {
        return cartDao.deleteCart()
    }

}