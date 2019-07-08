package com.juangomez.presentation.models

import android.content.res.Resources
import com.juangomez.presentation.R
import java.text.DecimalFormat

class CheckoutPresentationModel(
    val code: String,
    val name: String,
    price: String,
    quantity: String,
    offer: String?
) {
    val price: String = price
        get() = DecimalFormat("0.#").format(field.toFloat()) + "€"

    val quantity: String = quantity
        get() = "x${field}"

    val image: Int = (when (code) {
        "VOUCHER" -> R.drawable.voucher
        "TSHIRT" -> R.drawable.tshirt
        "MUG" -> R.drawable.mug
        else -> R.drawable.generic_code
    })

    val offerText: Int = (when (offer) {
        "TWO FOR ONE" -> R.string.two_for_one_offer
        "BULK" -> R.string.bulk_offer
        else -> R.string.empty
    })
}