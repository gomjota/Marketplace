package com.juangomez.data.sources.database

import com.juangomez.data.entities.ProductEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface DatabaseProductsSource {

    fun getProducts(): Flowable<List<ProductEntity>>

    fun insertProducts(products: List<ProductEntity>): Completable

    fun deleteProducts(): Completable
}