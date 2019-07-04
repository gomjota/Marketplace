package com.juangomez.domain.models.offer

import com.juangomez.domain.models.cart.Cart

abstract class Offer {
    abstract fun applyOffer(cart: Cart): Cart
}