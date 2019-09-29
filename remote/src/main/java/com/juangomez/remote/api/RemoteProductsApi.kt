package com.juangomez.remote.api

import com.juangomez.remote.responses.GetProductsResponse
import retrofit2.http.GET

interface RemoteProductsApi {

    @GET("bins/mujjd")
    suspend fun getProducts(): GetProductsResponse
}