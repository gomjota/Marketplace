package com.juangomez.data.sources.remote

import com.juangomez.data.entities.ProductEntity

interface RemoteProductsSource {
    suspend fun getProducts(): List<ProductEntity>
}