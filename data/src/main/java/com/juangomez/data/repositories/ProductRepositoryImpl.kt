package com.juangomez.data.repositories

import com.juangomez.data.mappers.toModel
import com.juangomez.data.sources.remote.RemoteProductsSource
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.ProductRepository

class ProductRepositoryImpl constructor(
    private val remote: RemoteProductsSource
) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        return remote.getProducts().toModel()

    }
}