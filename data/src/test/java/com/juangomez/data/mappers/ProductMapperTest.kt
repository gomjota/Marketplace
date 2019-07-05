package com.juangomez.data.mappers

import com.juangomez.data.entities.ProductEntity
import com.juangomez.data.mappers.toEntity
import com.juangomez.data.mappers.toModel
import com.juangomez.domain.models.product.Product
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `product and productEntity should have the same attributes`() {
        val product = Product("VOUCHER", "Cabify Voucher", 5f)
        val productEntity = ProductEntity(product.code, product.name, product.price)

        assert(product.toEntity() == productEntity)
    }

    @Test
    fun `productEntity and product should have the same attributes`() {
        val product = Product("VOUCHER", "Cabify Voucher", 5f)
        val productEntity = ProductEntity(product.code, product.name, product.price)

        assert(productEntity.toModel() == product)
    }
}