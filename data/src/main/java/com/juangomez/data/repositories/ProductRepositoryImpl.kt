package com.juangomez.data.repositories

import com.juangomez.data.mappers.toEntity
import com.juangomez.data.mappers.toModel
import com.juangomez.data.sources.database.DatabaseProductsSource
import com.juangomez.data.sources.remote.RemoteProductsSource
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.ProductRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class ProductRepositoryImpl constructor(
    private val remote: RemoteProductsSource,
    private val database: DatabaseProductsSource
) : ProductRepository {

    override fun getProducts(): Single<List<Product>> {
        return remote.getProducts().toModel()

    }

    override fun setProducts(products: List<Product>): Completable {
        return database.insertProducts(products.toEntity())
    }
}