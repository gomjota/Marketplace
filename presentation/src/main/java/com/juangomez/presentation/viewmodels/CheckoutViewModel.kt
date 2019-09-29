package com.juangomez.presentation.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.juangomez.domain.interactors.AddProductUseCase
import com.juangomez.domain.interactors.CreateCheckoutUseCase
import com.juangomez.domain.interactors.DeleteCartUseCase
import com.juangomez.domain.interactors.DeleteProductUseCase
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.checkout.Checkout
import com.juangomez.presentation.common.SingleLiveEvent
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

    val checkoutPrice = MutableLiveData<Float>()
    val paymentDone = SingleLiveEvent<Void>()
    val cartEmpty = SingleLiveEvent<Void>()
    val error = SingleLiveEvent<Void>()
    val checkoutProductsToShow = MediatorLiveData<List<CheckoutPresentationModel>>()

    private lateinit var cart: Cart

    fun prepare() {
        createCheckout()
    }

    private fun createCheckout() {
        val createCheckoutDisposable = CreateCheckoutSubscriber()
        createCheckoutUseCase.execute(createCheckoutDisposable)
        addDisposable(createCheckoutDisposable)
    }

    override fun onAddProductClicked(code: String) {
        val addProductDisposable = AddProductSubscriber()
        addProductUseCase.execute(
            addProductDisposable,
            cart.items.find { it.product.code == code }!!.product
        )
        addDisposable(addProductDisposable)
    }

    override fun onDeleteProductClicked(code: String) {
        val deleteProductDisposable = DeleteProductSubscriber()
        deleteProductUseCase.execute(
            deleteProductDisposable,
            cart.items.find { it.product.code == code }!!.product
        )
        addDisposable(deleteProductDisposable)
    }

    private fun deleteCart() {
        val deleteCartDisposable = DeleteCartSubscriber()
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
            checkoutProductsToShow.postValue(t.toPresentationModel())
            if (t.checkoutCart.items.isEmpty()) cartEmpty.call() else checkoutPrice.postValue(
                t.checkoutCart.totalPrice
            )
        }

        override fun onError(e: Throwable) {
            Logger.createCheckoutError()
            error.call()
        }

    }

    inner class AddProductSubscriber : DisposableCompletableObserver() {

        override fun onComplete() {
            Logger.addProductCompleted()
        }

        override fun onError(e: Throwable) {
            Logger.addProductError()
            error.call()
        }

    }

    inner class DeleteProductSubscriber : DisposableCompletableObserver() {

        override fun onComplete() {
            Logger.deleteProductCompleted()
        }

        override fun onError(e: Throwable) {
            Logger.deleteProductError()
            error.call()
        }
    }

    inner class DeleteCartSubscriber : DisposableCompletableObserver() {

        override fun onComplete() {
            Logger.deleteCartCompleted()
            paymentDone.call()
        }

        override fun onError(e: Throwable) {
            Logger.deleteCartError()
            error.call()
        }
    }
}

interface CheckoutListener {
    fun onAddProductClicked(code: String)

    fun onDeleteProductClicked(code: String)

    fun onPayClicked()
}