package com.juangomez.domain.repositories

import com.juangomez.domain.models.product.Product
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface ProductRepository {

    fun getProducts(): Single<List<Product>>

    fun setProducts(products: List<Product>): Completable
}