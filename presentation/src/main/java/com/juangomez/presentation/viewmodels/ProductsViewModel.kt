package com.juangomez.presentation.viewmodels

import android.util.Log
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
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subscribers.DisposableSubscriber

class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val getCartUseCase: GetCartUseCase
) : BaseViewModel(), ProductsListener {

    val isLoading = MutableLiveData<Boolean>()
    val isShowingEmptyCase = MutableLiveData<Boolean>()
    val productsInCart = MutableLiveData<Int>()
    val productsToShow = MediatorLiveData<List<ProductPresentationModel>>()
    val checkoutOpen = SingleLiveEvent<Void>()

    private var products: List<Product> = emptyList()

    fun prepare() {
        isLoading.postValue(true)

        val getProductsDisposable = GetProductsSubscriber()
        getProductsUseCase.execute(getProductsDisposable)
        addDisposable(getProductsDisposable)
    }

    fun initCartSubscriber() {
        val getCartDisposable = GetCartSubscriber()
        getCartUseCase.execute(getCartDisposable)
        addDisposable(getCartDisposable)
    }

    override fun onProductClicked(code: String) {
        val addProductDisposable = AddProductSubscriber()
        addProductUseCase.execute(addProductDisposable, products.find { it.code == code }!!)
        addDisposable(addProductDisposable)
    }

    override fun onCheckoutClicked() {
        checkoutOpen.postValue(null)
    }

    inner class GetProductsSubscriber : DisposableSingleObserver<List<Product>>() {
        override fun onSuccess(t: List<Product>) {
            isLoading.postValue(false)
            products = t
            productsToShow.postValue(t.toPresentationModel())
        }

        override fun onError(exception: Throwable) {
            isLoading.postValue(false)
            isShowingEmptyCase.postValue(true)
        }

    }

    inner class AddProductSubscriber : DisposableCompletableObserver() {

        override fun onComplete() {
            Log.d("COMPLETADO!", "COMPLETADO!")
        }

        override fun onError(e: Throwable) {
            Log.d("ERROR!", "ERROR!")
        }

    }

    inner class GetCartSubscriber : DisposableSubscriber<Cart>() {

        override fun onComplete() {

        }

        override fun onNext(t: Cart) {
            Log.d("OLE!", "PRODUCTO ANADIDO!")
            productsInCart.postValue(t.items.size)
        }

        override fun onError(t: Throwable?) {

        }
    }

}

interface ProductsListener {
    fun onProductClicked(code: String)

    fun onCheckoutClicked()
}