package com.juangomez.presentation.models

import com.juangomez.presentation.BuildConfig
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
        get() = DecimalFormat(BuildConfig.DEFAULT_PRICE_PATTERN)
            .format(field.toFloat()) + BuildConfig.DEFAULT_CURRENCY

    val quantity: String = quantity
        get() = "x${field}"

    val image: Int = (when (code) {
        "COPPER" -> R.drawable.copper
        "COMMANDER2" -> R.drawable.commander
        "PULSAR" -> R.drawable.pulsar
        else -> R.drawable.generic_code
    })

    val offerText: Int = (when (offer) {
        "TWO FOR ONE" -> R.string.two_for_one_offer
        "BULK" -> R.string.bulk_offer
        else -> R.string.empty
    })
}