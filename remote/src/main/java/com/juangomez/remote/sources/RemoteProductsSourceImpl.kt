package com.juangomez.remote.sources

import com.juangomez.data.entities.ProductEntity
import com.juangomez.data.sources.remote.RemoteProductsSource
import com.juangomez.remote.api.RemoteProductsApi
import com.juangomez.remote.mappers.toEntity

class RemoteProductsSourceImpl constructor(private val api: RemoteProductsApi) :
    RemoteProductsSource {

    override suspend fun getProducts(): List<ProductEntity> {
        return api.getProducts().toEntity()
    }
}