package com.juangomez.remote.sources

import com.juangomez.data.entities.ProductEntity
import com.juangomez.data.sources.remote.RemoteProductsSource
import com.juangomez.remote.api.RemoteProductsApi
import com.juangomez.remote.mappers.toEntity
import io.reactivex.Single

class RemoteProductsSourceImpl constructor(private val api: RemoteProductsApi): RemoteProductsSource {

    override fun getProducts(): Single<List<ProductEntity>> {
        return api.getProducts().toEntity()
    }
}