package com.juangomez.presentation.logger

import timber.log.Timber

object Logger {

    fun getProductsCompleted() {
        Timber.d("GET PRODUCTS COMPLETED")
    }

    fun getProductsError() {
        Timber.d("GET PRODUCTS COMPLETED")
    }

    fun getCartCompleted() {
        Timber.d("GET CART COMPLETED")
    }

    fun getCartNext() {
        Timber.d("GET CART NEXT")
    }

    fun getCartError() {
        Timber.d("GET CART COMPLETED")
    }

    fun createCheckoutCompleted() {
        Timber.d("CREATE CHECKOUT COMPLETED")
    }

    fun createCheckoutNext() {
        Timber.d("CREATE CHECKOUT NEXT")
    }

    fun createCheckoutError() {
        Timber.d("CREATE CHECKOUT ERROR")
    }

    fun addProductCompleted() {
        Timber.d("ADD PRODUCT COMPLETED")
    }

    fun addProductError() {
        Timber.d("ADD PRODUCT ERROR")
    }

    fun deleteProductCompleted() {
        Timber.d("DELETE PRODUCT COMPLETED")
    }

    fun deleteProductError() {
        Timber.d("DELETE PRODUCT ERROR")
    }

    fun deleteCartCompleted() {
        Timber.d("DELETE CART COMPLETED")
    }

    fun deleteCartError() {
        Timber.d("DELETE CART ERROR")
    }

}