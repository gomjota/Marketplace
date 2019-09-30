package com.juangomez.presentation.mappers

import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.checkout.Checkout
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.models.product.Product
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class CheckoutPresentationModelMapperTest {

    @Spy
    lateinit var twoForOneOffer: TwoForOneOffer

    @Spy
    lateinit var bulkOffer: BulkOffer

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `checkout presentation model should be shorted`() {
        val firstProduct = Product("FIRST", "PRODUCT", 100f)
        val secondsProduct = Product("SECOND", "PRODUCT", 100f)
        val cart = Cart(mutableListOf(CartItem(secondsProduct), CartItem(firstProduct)))
        val checkout = Checkout(cart, twoForOneOffer, bulkOffer)

        val checkoutPresentation = checkout.toPresentationModel()

        assert(checkoutPresentation.first().code == firstProduct.code)
    }

    @Test
    fun `checkout presentation model should be grouped by code`() {
        val firstProduct = Product("FIRST", "PRODUCT", 100f)
        val sameProduct = Product("FIRST", "PRODUCT", 100f)
        val cart = Cart(mutableListOf(CartItem(firstProduct), CartItem(sameProduct)))
        val checkout = Checkout(cart, twoForOneOffer, bulkOffer)

        val checkoutPresentation = checkout.toPresentationModel()

        assert(checkoutPresentation.size == 1)
    }

    @Test
    fun `checkout presentation model should have the same name than checkout model`() {
        val product = Product("FIRST", "PRODUCT", 100f)
        val cart = Cart(mutableListOf(CartItem(product)))
        val checkout = Checkout(cart, twoForOneOffer, bulkOffer)

        val checkoutPresentation = checkout.toPresentationModel()

        assert(checkoutPresentation.first().name == product.name)
    }
}