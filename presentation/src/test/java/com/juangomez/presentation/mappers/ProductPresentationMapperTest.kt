package com.juangomez.presentation.mappers

import com.juangomez.domain.models.product.Product
import com.juangomez.presentation.models.ProductPresentationModel
import junit.framework.Assert.assertEquals
import org.junit.Test

class ProductPresentationMapperTest {

    private val product = Product("CODE", "VOUCHER", 100f)
    private val expectedProduct = ProductPresentationModel("CODE", "VOUCHER", "100.0")

    @Test
    fun `product and product presentation model should have the same code`() {
        val productPresentationModel = product.toPresentationModel()

        assertEquals(productPresentationModel.code, expectedProduct.code)
    }

    @Test
    fun `product and product presentation model should have the same name`() {
        val productPresentationModel = product.toPresentationModel()

        assertEquals(productPresentationModel.name, expectedProduct.name)
    }

    @Test
    fun `product and product presentation model should have the same price`() {
        val productPresentationModel = product.toPresentationModel()

        assertEquals(productPresentationModel.price, expectedProduct.price)
    }
}