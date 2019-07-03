package com.juangomez.database.sources

import com.juangomez.data.entities.ProductEntity
import com.juangomez.data.sources.database.DatabaseProductsSource
import com.juangomez.database.dao.ProductDao
import com.juangomez.database.mappers.toDatabaseEntity
import com.juangomez.database.mappers.toEntity

class DatabaseProductsSourceImpl(private val productDao: ProductDao) : DatabaseProductsSource {

    override fun getProducts(): List<ProductEntity> {
        return productDao.getAll().toEntity()
    }

    override fun insertProducts(products: List<ProductEntity>) {
        productDao.insertAll(products.toDatabaseEntity())
    }

    override fun deleteProducts() {
        productDao.deleteAll()
    }

}