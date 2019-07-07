package com.juangomez.presentation.models

import com.juangomez.presentation.R

class CheckoutPresentationModel(
    val code: String,
    val name: String,
    price: String,
    quantity: String
) {
    val price: String = price
        get() = "${field}€"

    val quantity: String = quantity
        get() = "x${field}"

    val image: Int = (when (code) {
        "VOUCHER" -> R.drawable.voucher
        "TSHIRT" -> R.drawable.tshirt
        "MUG" -> R.drawable.mug
        else -> R.drawable.generic_code
    })
}