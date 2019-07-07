package com.juangomez.database.sources

import com.juangomez.data.entities.CartEntity
import com.juangomez.data.sources.database.DatabaseCartSource
import com.juangomez.database.MarketplaceDatabase
import com.juangomez.database.dao.CartDao
import com.juangomez.database.mappers.toDatabaseEntity
import com.juangomez.database.mappers.toEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class DatabaseCartSourceImpl(private val database: MarketplaceDatabase) : DatabaseCartSource {

    private val cartDao: CartDao = database.cartDao()

    override fun getCart(): Flowable<CartEntity> {
        return cartDao.getCart().map {
            if (it.isEmpty()) CartEntity(emptyList())
            else it[0].toEntity()
        }
    }

    override fun insertCart(cart: CartEntity): Completable {
        return cartDao.insertCart(cart.toDatabaseEntity())
    }

    override fun deleteCart(): Completable {
        return cartDao.deleteCart()
    }

}