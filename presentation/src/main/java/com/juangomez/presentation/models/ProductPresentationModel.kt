package com.juangomez.presentation.models

import com.juangomez.presentation.BuildConfig
import com.juangomez.presentation.R
import java.text.DecimalFormat

class ProductPresentationModel(
    val code: String,
    val name: String,
    price: String
) {
    val price: String = price
        get() = DecimalFormat(BuildConfig.DEFAULT_PRICE_PATTERN)
            .format(field.toFloat()) + BuildConfig.DEFAULT_CURRENCY

    val image: Int = (when (code) {
        "COPPER" -> R.drawable.copper
        "COMMANDER2" -> R.drawable.commander
        "PULSAR" -> R.drawable.pulsar
        else -> R.drawable.generic_code
    })
}