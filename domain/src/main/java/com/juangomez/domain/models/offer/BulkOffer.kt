package com.juangomez.domain.models.offer

import com.juangomez.domain.models.cart.Cart
import java.time.LocalDate

class BulkOffer : Offer() {

    private val OFFER_CODE = "BULK"
    private val EXPIRATION = LocalDate.parse("2020-01-01")
    private val PRODUCT_CODE = "COMMANDER2"
    private val PRICE_PER_UNIT = 1000f

    override fun applyOffer(cart: Cart): Cart {
        if (LocalDate.now() > EXPIRATION) return cart
        val cartAfterOffer = Cart(mutableListOf())

        cartAfterOffer.items = cart.items
            .map { it.copy(product = it.product, cartPrice = it.cartPrice) }
            .toMutableList()

        cartAfterOffer.items
            .filter { it.product.code == PRODUCT_CODE }
            .takeIf { it.count() >= 3 }
            ?.map {
                it.cartPrice = PRICE_PER_UNIT
                it.offerApplied = OFFER_CODE
                it
            }
            ?.union(cart.items)
            ?.toMutableList()

        return cartAfterOffer
    }
}