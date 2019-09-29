package com.juangomez.domain.repositories

import com.juangomez.domain.models.product.Product

interface ProductRepository {

    suspend fun getProducts(): List<Product>
}