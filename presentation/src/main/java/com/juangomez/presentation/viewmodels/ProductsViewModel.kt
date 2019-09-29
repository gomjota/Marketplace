package com.juangomez.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.juangomez.domain.interactors.AddProductUseCase
import com.juangomez.domain.interactors.GetCartUseCase
import com.juangomez.domain.interactors.GetProductsUseCase
import com.juangomez.domain.interactors.base.BaseUseCase
import com.juangomez.domain.models.base.Failure
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.product.Product
import com.juangomez.presentation.logger.Logger
import com.juangomez.presentation.mappers.toPresentationModel
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.viewmodels.base.BaseViewModel

open class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val getCartUseCase: GetCartUseCase
) : BaseViewModel(), ProductsListener {

    sealed class ProductsState {
        object Loading : ProductsState()
        object Empty : ProductsState()
        object Error : ProductsState()
        object Checkout : ProductsState()
        data class Cart(val productsInCart: Int) : ProductsState()
        data class Products(val products: List<ProductPresentationModel>) : ProductsState()
    }

    val state = MutableLiveData<ProductsState>().apply {
        this.value = ProductsState.Loading
    }

    var products: List<Product> = emptyList()

    fun prepare() {
        getProductsUseCase.invoke(viewModelScope) {
            it.either(
                ::handleError,
                ::handleGetProductsSuccess
            )
        }
    }

    fun getCart() {
        getCartUseCase.invoke(viewModelScope) { it.either(::handleError, ::handleGetCartSuccess) }
    }

    override fun onProductClicked(code: String) {
        val productToAdd = products.find { it.code == code }!!
        addProductUseCase.invoke(
            viewModelScope,
            AddProductUseCase.Params(productToAdd)
        ) { it.either(::handleError, ::handleAddProductSuccess) }
    }

    private fun handleGetProductsSuccess(products: List<Product>) {
        Logger.getProductsCompleted()
        this.products = products

        when {
            products.isEmpty() -> state.value = ProductsState.Empty
            products.isNotEmpty() -> state.value =
                ProductsState.Products(products.toPresentationModel())
        }
    }

    private fun handleGetCartSuccess(cart: Cart) {
        Logger.getCartCompleted()
        state.value = ProductsState.Cart(cart.items.size)
    }

    private fun handleAddProductSuccess(none: BaseUseCase.None) {
        Logger.addProductCompleted()
        getCart()
    }

    private fun handleError(failure: Failure) {
        Logger.createCheckoutError()
        state.value = ProductsState.Error
    }

    override fun onCheckoutClicked() {
        state.value = ProductsState.Checkout
    }
}

interface ProductsListener {
    fun onProductClicked(code: String)

    fun onCheckoutClicked()
}