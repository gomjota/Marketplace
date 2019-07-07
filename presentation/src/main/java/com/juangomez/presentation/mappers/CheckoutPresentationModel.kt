package com.juangomez.presentation.mappers

import com.juangomez.domain.models.checkout.Checkout
import com.juangomez.presentation.models.CheckoutPresentationModel

fun Checkout.toPresentationModel(): List<CheckoutPresentationModel> {
    return checkoutCart.items
        .groupBy { it.product.code }.entries
        .map {
            CheckoutPresentationModel(
                it.key,
                it.value[0].product.name,
                it.value.sumByDouble { it.cartPrice.toDouble() }.toString(),
                it.value.size.toString()
            )
        }.sortedBy { it.code }
}