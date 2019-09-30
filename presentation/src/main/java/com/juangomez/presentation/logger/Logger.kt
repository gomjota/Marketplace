package com.juangomez.presentation.logger

import timber.log.Timber

object Logger {

    fun getProductsCompleted() {
        Timber.d("GET PRODUCTS COMPLETED")
    }

    fun getCartCompleted() {
        Timber.d("GET CART COMPLETED")
    }

    fun createCheckoutCompleted() {
        Timber.d("CREATE CHECKOUT COMPLETED")
    }

    fun createCheckoutError() {
        Timber.d("CREATE CHECKOUT ERROR")
    }

    fun addProductCompleted() {
        Timber.d("ADD PRODUCT COMPLETED")
    }

    fun deleteProductCompleted() {
        Timber.d("DELETE PRODUCT COMPLETED")
    }

    fun deleteCartError() {
        Timber.d("DELETE CART ERROR")
    }
}