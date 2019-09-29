package com.juangomez.presentation.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.juangomez.domain.interactors.AddProductUseCase
import com.juangomez.domain.interactors.GetCartUseCase
import com.juangomez.domain.interactors.GetProductsUseCase
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.product.Product
import com.juangomez.presentation.common.SingleLiveEvent
import com.juangomez.presentation.logger.Logger
import com.juangomez.presentation.mappers.toPresentationModel
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.viewmodels.base.BaseViewModel
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subscribers.DisposableSubscriber
import timber.log.Timber

open class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val getCartUseCase: GetCartUseCase
) : BaseViewModel(), ProductsListener {

    val isLoading = MutableLiveData<Boolean>()
    val isShowingEmptyCase = MutableLiveData<Boolean>()
    val productsInCart = MutableLiveData<Int>()
    val productsToShow = MediatorLiveData<List<ProductPresentationModel>>()
    val checkoutOpen = SingleLiveEvent<Void>()
    val error = SingleLiveEvent<Void>()

    lateinit var getProductsDisposable: GetProductsSubscriber
    lateinit var getCartDisposable: GetCartSubscriber
    lateinit var addProductDisposable: AddProductSubscriber

    var products: List<Product> = emptyList()

    fun prepare() {
        isLoading.postValue(true)

        getProductsDisposable = GetProductsSubscriber()
        getProductsUseCase.execute(getProductsDisposable)
        addDisposable(getProductsDisposable)
    }

    fun initCartSubscriber() {
        getCartDisposable = GetCartSubscriber()
        getCartUseCase.execute(getCartDisposable)
        addDisposable(getCartDisposable)
    }

    override fun onProductClicked(code: String) {
        addProductDisposable = AddProductSubscriber()
        addProductUseCase.execute(addProductDisposable, products.find { it.code == code }!!)
        addDisposable(addProductDisposable)
    }

    override fun onCheckoutClicked() {
        checkoutOpen.call()
    }

    inner class GetProductsSubscriber : DisposableSingleObserver<List<Product>>() {

        override fun onSuccess(t: List<Product>) {
            Logger.getProductsCompleted()
            isLoading.postValue(false)
            products = t
            if (products.isNotEmpty()) {
                productsToShow.postValue(t.toPresentationModel())
            } else {
                isShowingEmptyCase.postValue(true)
            }
        }

        override fun onError(exception: Throwable) {
            Logger.getProductsError()
            isLoading.postValue(false)
            error.call()
            isShowingEmptyCase.postValue(true)
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

    inner class GetCartSubscriber : DisposableSubscriber<Cart>() {

        override fun onComplete() {
            Logger.getCartCompleted()
        }

        override fun onNext(t: Cart) {
            Logger.getCartNext()
            productsInCart.postValue(t.items.size)
        }

        override fun onError(t: Throwable?) {
            Logger.getCartError()
            error.call()
        }
    }

}

interface ProductsListener {
    fun onProductClicked(code: String)

    fun onCheckoutClicked()
}