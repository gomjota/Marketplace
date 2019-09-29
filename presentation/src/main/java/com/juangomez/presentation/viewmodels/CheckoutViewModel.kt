package com.juangomez.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.juangomez.domain.interactors.AddProductUseCase
import com.juangomez.domain.interactors.CreateCheckoutUseCase
import com.juangomez.domain.interactors.DeleteCartUseCase
import com.juangomez.domain.interactors.DeleteProductUseCase
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

    lateinit var createCheckoutDisposable: CreateCheckoutSubscriber
    lateinit var addProductDisposable: AddProductSubscriber
    lateinit var deleteProductDisposable: DeleteProductSubscriber
    lateinit var deleteCartDisposable: DeleteCartSubscriber

    lateinit var cart: Cart

    fun prepare() {
        createCheckout()
    }

    private fun createCheckout() {
        createCheckoutDisposable = CreateCheckoutSubscriber()
        createCheckoutUseCase.execute(createCheckoutDisposable)
        addDisposable(createCheckoutDisposable)
    }

    override fun onAddProductClicked(code: String) {
        addProductDisposable = AddProductSubscriber()
        addProductUseCase.execute(
            addProductDisposable,
            cart.items.find { it.product.code == code }!!.product
        )
        addDisposable(addProductDisposable)
    }

    override fun onDeleteProductClicked(code: String) {
        deleteProductDisposable = DeleteProductSubscriber()
        deleteProductUseCase.execute(
            deleteProductDisposable,
            cart.items.find { it.product.code == code }!!.product
        )
        addDisposable(deleteProductDisposable)
    }

    private fun deleteCart() {
        deleteCartDisposable = DeleteCartSubscriber()
        deleteCartUseCase.execute(deleteCartDisposable)
        addDisposable(deleteCartDisposable)
    }

    override fun onPayClicked() {
        deleteCart()
    }

    inner class CreateCheckoutSubscriber : DisposableSubscriber<Checkout>() {

        override fun onComplete() {
            Logger.createCheckoutCompleted()
        }

        override fun onNext(t: Checkout?) {
            Logger.createCheckoutNext()
            cart = t!!.checkoutCart

            when {
                t.checkoutCart.items.isEmpty() -> state.value = CheckoutState.Empty
                t.checkoutCart.items.isNotEmpty() -> state.value =
                    CheckoutState.Cart(t.toPresentationModel(), t.checkoutCart.totalPrice)
            }
        }

        override fun onError(e: Throwable) {
            Logger.createCheckoutError()
            state.value = CheckoutState.Error
        }

    }

    inner class AddProductSubscriber : DisposableCompletableObserver() {

        override fun onComplete() {
            Logger.addProductCompleted()
        }

        override fun onError(e: Throwable) {
            Logger.addProductError()
            state.value = CheckoutState.Error
        }

    }

    inner class DeleteProductSubscriber : DisposableCompletableObserver() {

        override fun onComplete() {
            Logger.deleteProductCompleted()
        }

        override fun onError(e: Throwable) {
            Logger.deleteProductError()
            state.value = CheckoutState.Error
        }
    }

    inner class DeleteCartSubscriber : DisposableCompletableObserver() {

        override fun onComplete() {
            Logger.deleteCartCompleted()
            state.value = CheckoutState.Complete
        }

        override fun onError(e: Throwable) {
            Logger.deleteCartError()
            state.value = CheckoutState.Error
        }
    }
}

interface CheckoutListener {
    fun onAddProductClicked(code: String)

    fun onDeleteProductClicked(code: String)

    fun onPayClicked()
}