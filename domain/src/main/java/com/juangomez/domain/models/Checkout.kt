package com.juangomez.domain.models

import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer

data class Checkout(
    private val cart: Cart,
    private val twoForOneOffer: TwoForOneOffer,
    private val bulkOffer: BulkOffer
) {

    var checkoutCart = generateCartAfterOffers()
        private set

    private fun generateCartAfterOffers(): Cart {
        var cart = twoForOneOffer.applyOffer(cart)
        cart = bulkOffer.applyOffer(cart)
        return cart
    }
}