package com.juangomez.persistence.sources

import com.juangomez.data.entities.CartEntity
import com.juangomez.data.sources.persistence.DatabaseCartSource
import com.juangomez.persistence.MarketplaceDatabase
import com.juangomez.persistence.dao.CartDao
import com.juangomez.persistence.mappers.toDatabaseEntity
import com.juangomez.persistence.mappers.toEntity

class DatabaseCartSourceImpl(private val database: MarketplaceDatabase) : DatabaseCartSource {

    private val cartDao: CartDao = database.cartDao()

    override suspend fun getCart(): CartEntity {
        val cart = cartDao.getCart()
        return if (cart.isEmpty()) CartEntity(emptyList()) else cart.last().toEntity()
    }

    override suspend fun insertCart(cart: CartEntity) {
        cartDao.insertCart(cart.toDatabaseEntity())
    }

    override suspend fun deleteCart() {
        cartDao.deleteCart()
    }

}