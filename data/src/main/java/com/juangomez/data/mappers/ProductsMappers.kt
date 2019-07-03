package com.juangomez.data.mappers

import com.juangomez.data.entities.ProductEntity
import com.juangomez.data.responses.ProductsResponse

class ProductsDataEntityMapper constructor() {

    fun mapProductsToEntity(articles: List<ProductEntity>?)
            : List<ProductEntity> = articles?.map { mapArticleToEntity(it) } ?: emptyList()

    fun mapArticleToEntity(response: ProductsPublisherData): ProductsPublisherEntity = ProductsPublisherEntity(
        id = response.id,
        name = response.name,
        description = response.description,
        url = response.url,
        category = response.category
    )


}


class ProductEntityDataMapper constructor() {

    fun mapToEntity(data: ProductEntity?): ProductsResponse? = ProductsResponse(
        status = data?.status,
        articles = mapProductsToEntity(data?.articles)
    )

    fun mapProductsToEntity(articles: List<ProductsPublisherEntity>?)
            : List<ProductsPublisherData> = articles?.map { mapArticleToEntity(it) } ?: emptyList()

    fun mapArticleToEntity(response: ProductsPublisherEntity): ProductsPublisherData = ProductsPublisherData(
        id = response.id,
        name = response.name,
        description = response.description,
        url = response.url,
        category = response.category
    )


}