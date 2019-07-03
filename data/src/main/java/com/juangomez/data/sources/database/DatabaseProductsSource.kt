package com.juangomez.data.sources.database

import com.juangomez.data.entities.ProductEntity

interface DatabaseProductsSource {

    fun getProducts(): List<ProductEntity>

    fun insertProducts(products: List<ProductEntity>)

    fun deleteProducts()
}