package com.juangomez.data.mappers

import com.juangomez.data.entities.CartEntity
import com.juangomez.data.entities.ProductEntity
import com.juangomez.data.mappers.toCartItem
import com.juangomez.data.mappers.toEntity
import com.juangomez.data.mappers.toModel
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.product.Product
import org.junit.Test

class CartMapperTest {

    @Test
    fun `cartItem and productEntity should have the same attributes`() {
        val product = Product("COPPER", "COPPER", 5f)
        val cartItem = CartItem(product = product)
        val productEntity = ProductEntity(product.code, product.name, product.price)

        assert(cartItem.toEntity() == productEntity)
    }

    @Test
    fun `productEntity and cartItem productEntity should have the same attributes`() {
        val product = Product("COPPER", "COPPER", 5f)
        val cartItem = CartItem(product = product)
        val productEntity = ProductEntity(product.code, product.name, product.price)

        assert(productEntity.toCartItem() == cartItem)
    }

    @Test
    fun `cart and cartEntity should have the same attributes`() {
        val product = Product("COPPER", "COPPER", 5f)
        val cartItem = CartItem(product = product)
        val productEntity = ProductEntity(product.code, product.name, product.price)
        val cart = Cart(mutableListOf(cartItem))
        val cartEntity = CartEntity(listOf(productEntity))

        assert(cart.toEntity() == cartEntity)
    }

    @Test
    fun `cartEntity and cart should have the same attributes`() {
        val product = Product("COPPER", "COPPER", 5f)
        val cartItem = CartItem(product = product)
        val productEntity = ProductEntity(product.code, product.name, product.price)
        val cart = Cart(mutableListOf(cartItem))
        val cartEntity = CartEntity(listOf(productEntity))

        assert(cartEntity.toModel() == cart)
    }
}