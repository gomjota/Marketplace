package com.juangomez.data.repositories

import com.juangomez.data.mappers.toModel
import com.juangomez.data.sources.remote.RemoteProductsSource
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.ProductRepository
import io.reactivex.Single

class ProductRepositoryImpl constructor(
    private val remote: RemoteProductsSource
) : ProductRepository {

    override fun getProducts(): Single<List<Product>> {
        return remote.getProducts().toModel()

    }
}