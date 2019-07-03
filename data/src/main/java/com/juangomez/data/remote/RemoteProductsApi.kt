package com.juangomez.data.remote

import com.juangomez.data.responses.ProductsResponse
import io.reactivex.Flowable
import retrofit2.http.GET

interface RemoteNewsApi {

    @GET("bins/4bwec")
    fun getProducts(): Flowable<ProductsResponse>

}