package com.juangomez.domain

import com.juangomez.domain.models.checkout.Checkout
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.models.product.Product
import org.junit.Test

class CheckoutTest {

    @Test
    fun `should be the same cart with no offers`() {
        val products = mutableListOf(
            CartItem(Product("COPPER", "COPPER", 5f)),
            CartItem(Product("COMMANDER2", "COMMANDER", 20f)),
            CartItem(Product("PULSAR", "PULSAR", 7.5f))
        )

        val cart = Cart(products)

        val twoForOneOffer = TwoForOneOffer()
        val bulkOffer = BulkOffer()

        val checkout = Checkout(cart, twoForOneOffer, bulkOffer)
        val finalCart = checkout.checkoutCart

        assert(cart.totalPrice == finalCart.totalPrice)
    }

    @Test
    fun `should be different carts with bulk offer`() {
        val products = mutableListOf(
            CartItem(Product("COPPER", "COPPER", 5f)),
            CartItem(Product("COMMANDER2", "COMMANDER", 1200f)),
            CartItem(Product("COMMANDER2", "COMMANDER", 1200f)),
            CartItem(Product("COMMANDER2", "COMMANDER", 1200f)),
            CartItem(Product("PULSAR", "PULSAR", 7.5f))
        )

        val cart = Cart(products)

        val bulkOffer = BulkOffer()
        val twoForOneOffer = TwoForOneOffer()

        val checkout = Checkout(cart, twoForOneOffer, bulkOffer)
        val finalCart = checkout.checkoutCart

        assert(cart.totalPrice == 3612.5f)
        assert(finalCart.totalPrice == 3012.5f)
    }

    @Test
    fun `should be different carts with two for one offer`() {
        val products = mutableListOf(
            CartItem(Product("COPPER", "COPPER", 5f)),
            CartItem(Product("COPPER", "COPPER", 5f)),
            CartItem(Product("COMMANDER2", "COMMANDER", 20f)),
            CartItem(Product("PULSAR", "PULSAR", 7.5f))
        )

        val cart = Cart(products)

        val bulkOffer = BulkOffer()
        val twoForOneOffer = TwoForOneOffer()

        val checkout = Checkout(cart, twoForOneOffer, bulkOffer)
        val finalCart = checkout.checkoutCart

        assert(cart.totalPrice == 37.5f)
        assert(finalCart.totalPrice == 32.5f)
    }

    @Test
    fun `should be different carts with both offers`() {
        val products = mutableListOf(
            CartItem(Product("COPPER", "COPPER", 5f)),
            CartItem(Product("COPPER", "COPPER", 5f)),
            CartItem(Product("COMMANDER2", "COMMANDER", 1200f)),
            CartItem(Product("COMMANDER2", "COMMANDER", 1200f)),
            CartItem(Product("COMMANDER2", "COMMANDER", 1200f)),
            CartItem(Product("PULSAR", "PULSAR", 7.5f)),
            CartItem(Product("PULSAR", "PULSAR", 7.5f))
        )

        val cart = Cart(products)

        val bulkOffer = BulkOffer()
        val twoForOneOffer = TwoForOneOffer()

        val checkout = Checkout(cart, twoForOneOffer, bulkOffer)
        val finalCart = checkout.checkoutCart

        assert(cart.totalPrice == 3625f)
        assert(finalCart.totalPrice == 3020f)
    }
}