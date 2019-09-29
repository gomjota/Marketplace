package com.juangomez.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.juangomez.domain.interactors.AddProductUseCase
import com.juangomez.domain.interactors.CreateCheckoutUseCase
import com.juangomez.domain.interactors.DeleteCartUseCase
import com.juangomez.domain.interactors.DeleteProductUseCase
import com.juangomez.domain.interactors.base.BaseUseCase
import com.juangomez.domain.models.base.Failure
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.checkout.Checkout
import com.juangomez.presentation.logger.Logger
import com.juangomez.presentation.mappers.toPresentationModel
import com.juangomez.presentation.models.CheckoutPresentationModel
import com.juangomez.presentation.viewmodels.base.BaseViewModel
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.subscribers.DisposableSubscriber

class CheckoutViewModel(
    private val addProductUseCase: AddProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val createCheckoutUseCase: CreateCheckoutUseCase
) : BaseViewModel(), CheckoutListener {

    sealed class CheckoutState {
        object Complete : CheckoutState()
        object Empty : CheckoutState()
        object Error : CheckoutState()
        data class Cart(val products: List<CheckoutPresentationModel>, val price: Float) :
            CheckoutState()
    }

    val state = MutableLiveData<CheckoutState>()

    lateinit var cart: Cart

    fun prepare() {
        createCheckout()
    }

    private fun createCheckout() {
        createCheckoutUseCase.invoke(viewModelScope) { it.either(::handleError, ::handleCreateCheckoutSuccess) }
    }

    override fun onAddProductClicked(code: String) {
        val productToAdd = cart.items.find { it.product.code == code }!!.product
        addProductUseCase.invoke(viewModelScope, AddProductUseCase.Params(productToAdd)) { it.either(::handleError, ::handleAddProductToCartSuccess) }
    }

    override fun onDeleteProductClicked(code: String) {
        val productToDelete = cart.items.find { it.product.code == code }!!.product
        deleteProductUseCase.invoke(viewModelScope, DeleteProductUseCase.Params(productToDelete)) { it.either(::handleError, ::handleDeleteProductFromCartSuccess) }
    }

    private fun deleteCart() {
        deleteCartUseCase.invoke(viewModelScope) { it.either(::handleError, ::handleDeleteCartSuccess) }
    }

    override fun onPayClicked() {
        deleteCart()
    }

    private fun handleCreateCheckoutSuccess(checkout: Checkout) {
        Logger.createCheckoutCompleted()
        cart = checkout.checkoutCart

        when {
            checkout.checkoutCart.items.isEmpty() -> state.value = CheckoutState.Empty
            checkout.checkoutCart.items.isNotEmpty() -> state.value =
                CheckoutState.Cart(checkout.toPresentationModel(), checkout.checkoutCart.totalPrice)
        }
    }

    private fun handleAddProductToCartSuccess(emptyResponse: BaseUseCase.None) {
        Logger.addProductCompleted()
        createCheckout()
    }

    private fun handleDeleteProductFromCartSuccess(emptyResponse: BaseUseCase.None) {
        Logger.deleteProductCompleted()
        createCheckout()
    }

    private fun handleDeleteCartSuccess(emptyResponse: BaseUseCase.None) {
        Logger.deleteCartError()
    }

    private fun handleError(failure: Failure) {
        Logger.createCheckoutError()
        state.value = CheckoutState.Error
    }
}

interface CheckoutListener {
    fun onAddProductClicked(code: String)

    fun onDeleteProductClicked(code: String)

    fun onPayClicked()
}