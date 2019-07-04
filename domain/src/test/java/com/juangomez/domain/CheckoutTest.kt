package com.juangomez.domain

import com.juangomez.domain.models.checkout.Checkout
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.models.product.Product
import org.junit.Test

class CheckoutTest {

    @Test
    fun `should be the same cart with no offers`() {
        val cart = Cart()
        cart.addProduct(Product("VOUCHER", "Cabify Voucher", 5f))
        cart.addProduct(Product("TSHIRT", "Cabify T-Shirt", 20f))
        cart.addProduct(Product("MUG", "Cabify Coffee Mug", 7.5f))

        val twoForOneOffer = TwoForOneOffer()
        val bulkOffer = BulkOffer()

        val checkout = Checkout(cart, twoForOneOffer, bulkOffer)
        val finalCart = checkout.checkoutCart

        assert(cart.totalPrice == finalCart.totalPrice)
    }

    @Test
    fun `should be different carts with two for one offer`() {
        val cart = Cart()
        cart.addProduct(Product("VOUCHER", "Cabify Voucher", 5f))
        cart.addProduct(Product("TSHIRT", "Cabify T-Shirt", 20f))
        cart.addProduct(Product("TSHIRT", "Cabify T-Shirt", 20f))
        cart.addProduct(Product("TSHIRT", "Cabify T-Shirt", 20f))
        cart.addProduct(Product("MUG", "Cabify Coffee Mug", 7.5f))

        val bulkOffer = BulkOffer()
        val twoForOneOffer = TwoForOneOffer()

        val checkout = Checkout(cart, twoForOneOffer, bulkOffer)
        val finalCart = checkout.checkoutCart

        assert(cart.totalPrice == 72.5f)
        assert(finalCart.totalPrice == 69.5f)
    }

    @Test
    fun `should be different carts with bulk offer`() {
        val cart = Cart()
        cart.addProduct(Product("VOUCHER", "Cabify Voucher", 5f))
        cart.addProduct(Product("VOUCHER", "Cabify Voucher", 5f))
        cart.addProduct(Product("TSHIRT", "Cabify T-Shirt", 20f))
        cart.addProduct(Product("MUG", "Cabify Coffee Mug", 7.5f))

        val bulkOffer = BulkOffer()
        val twoForOneOffer = TwoForOneOffer()

        val checkout = Checkout(cart, twoForOneOffer, bulkOffer)
        val finalCart = checkout.checkoutCart

        assert(cart.totalPrice == 37.5f)
        assert(finalCart.totalPrice == 32.5f)
    }

    @Test
    fun `should be different carts with both offers`() {
        val cart = Cart()
        cart.addProduct(Product("VOUCHER", "Cabify Voucher", 5f))
        cart.addProduct(Product("VOUCHER", "Cabify Voucher", 5f))
        cart.addProduct(Product("TSHIRT", "Cabify T-Shirt", 20f))
        cart.addProduct(Product("TSHIRT", "Cabify T-Shirt", 20f))
        cart.addProduct(Product("TSHIRT", "Cabify T-Shirt", 20f))
        cart.addProduct(Product("MUG", "Cabify Coffee Mug", 7.5f))
        cart.addProduct(Product("MUG", "Cabify Coffee Mug", 7.5f))

        val bulkOffer = BulkOffer()
        val twoForOneOffer = TwoForOneOffer()

        val checkout = Checkout(cart, twoForOneOffer, bulkOffer)
        val finalCart = checkout.checkoutCart

        assert(cart.totalPrice == 85f)
        assert(finalCart.totalPrice == 77f)
    }
}