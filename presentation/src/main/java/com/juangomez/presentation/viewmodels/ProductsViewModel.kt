package com.juangomez.presentation.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.juangomez.domain.interactors.AddProductUseCase
import com.juangomez.domain.interactors.GetCartUseCase
import com.juangomez.domain.interactors.GetProductsUseCase
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.product.Product
import com.juangomez.presentation.common.SingleLiveEvent
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

    var getProductsDisposable = GetProductsSubscriber()
    var getCartDisposable = GetCartSubscriber()
    var addProductDisposable = AddProductSubscriber()

    var products: List<Product> = emptyList()

    fun prepare() {
        isLoading.postValue(true)

        getProductsUseCase.execute(getProductsDisposable)
        addDisposable(getProductsDisposable)
    }

    fun initCartSubscriber() {
        getCartUseCase.execute(getCartDisposable)
        addDisposable(getCartDisposable)
    }

    override fun onProductClicked(code: String) {
        addProductUseCase.execute(addProductDisposable, products.find { it.code == code }!!)
        addDisposable(addProductDisposable)
    }

    override fun onCheckoutClicked() {
        checkoutOpen.call()
    }

    inner class GetProductsSubscriber : DisposableSingleObserver<List<Product>>() {
        override fun onSuccess(t: List<Product>) {
            Timber.d("GET PRODUCTS SUCCESS")
            isLoading.postValue(false)
            products = t
            if (products.isNotEmpty()) {
                productsToShow.postValue(t.toPresentationModel())
            } else {
                isShowingEmptyCase.postValue(true)
            }
        }

        override fun onError(exception: Throwable) {
            Timber.d("ERROR GETTING PRODUCTS")
            isLoading.postValue(false)
            error.call()
            isShowingEmptyCase.postValue(true)
        }

    }

    inner class AddProductSubscriber : DisposableCompletableObserver() {

        override fun onComplete() {
            Timber.d("ADD PRODUCT COMPLETED")
        }

        override fun onError(e: Throwable) {
            Timber.d("ERROR ADDING PRODUCT")
            error.call()
        }

    }

    inner class GetCartSubscriber : DisposableSubscriber<Cart>() {

        override fun onComplete() {
            Timber.d("GET CART COMPLETED")
        }

        override fun onNext(t: Cart) {
            Timber.d("GET CART NEXT")
            productsInCart.postValue(t.items.size)
        }

        override fun onError(t: Throwable?) {
            Timber.d("ERROR GETTING CART")
            error.call()
        }
    }

}

interface ProductsListener {
    fun onProductClicked(code: String)

    fun onCheckoutClicked()
}