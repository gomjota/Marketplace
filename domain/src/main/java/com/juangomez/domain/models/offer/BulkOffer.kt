package com.juangomez.domain.models.offer

import com.juangomez.domain.models.cart.Cart
import java.time.LocalDate

class BulkOffer : Offer() {

    private val EXPIRATION = LocalDate.parse("2020-01-01")
    private val CODE = "TSHIRT"
    private val PRICE_PER_UNIT = 19f

    override fun applyOffer(cart: Cart): Cart {
        if (LocalDate.now() > EXPIRATION) return cart
        val cartAfterOffer = Cart()
        cartAfterOffer.items = cart.items
            .map { it.copy(product = it.product, cartPrice = it.cartPrice) }
            .toMutableList()

        cartAfterOffer.items
            .filter { it.product.code == CODE }
            .takeIf { it.count() >= 3 }
            ?.map {
                it.cartPrice = PRICE_PER_UNIT
                it
            }
            ?.union(cart.items)
            ?.toMutableList()

        return cartAfterOffer
    }
}