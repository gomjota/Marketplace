package com.juangomez.domain.models.offer

import com.juangomez.domain.models.cart.Cart
import java.time.LocalDate

class TwoForOneOffer : Offer() {

    private val EXPIRATION = LocalDate.parse("2020-01-01")
    private val CODE = "VOUCHER"

    override fun applyOffer(cart: Cart): Cart {
        if (LocalDate.now() > EXPIRATION) return cart
        val cartAfterOffer = Cart()

        cartAfterOffer.items = cart.items
            .map { it.copy(product = it.product, cartPrice = it.cartPrice) }
            .toMutableList()

        cartAfterOffer.items
            .filter { it.product.code == CODE }
            .takeIf { it.count() % 2 == 0 }
            ?.mapIndexed { index, item ->
                if (index % 2 == 0) {
                    item.cartPrice = 0f
                }
                item
            }
            ?.toMutableList()

        return cartAfterOffer
    }
}