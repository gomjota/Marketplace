package com.juangomez.remote.responses

import com.juangomez.remote.entities.RemoteProductEntity

data class GetProductsResponse(
    val products: List<RemoteProductEntity>
)