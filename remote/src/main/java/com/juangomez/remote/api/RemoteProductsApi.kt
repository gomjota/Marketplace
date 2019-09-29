package com.juangomez.remote.api

import com.juangomez.remote.responses.GetProductsResponse
import io.reactivex.Single
import retrofit2.http.GET

interface RemoteProductsApi {

    @GET("bins/mujjd")
    fun getProducts(): Single<GetProductsResponse>
}