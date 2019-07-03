package com.juangomez.data.sources.remote

import com.juangomez.data.entities.ProductEntity
import io.reactivex.Single

interface RemoteProductsSource {
    fun getProducts(): Single<List<ProductEntity>>
}