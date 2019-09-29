package com.juangomez.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.juangomez.domain.interactors.AddProductUseCase
import com.juangomez.domain.interactors.GetCartUseCase
import com.juangomez.domain.interactors.GetProductsUseCase
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.product.Product
import com.juangomez.presentation.logger.Logger
import com.juangomez.presentation.mappers.toPresentationModel
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.viewmodels.base.BaseViewModel
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subscribers.DisposableSubscriber

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

    lateinit var getProductsDisposable: GetProductsSubscriber
    lateinit var getCartDisposable: GetCartSubscriber
    lateinit var addProductDisposable: AddProductSubscriber

    var products: List<Product> = emptyList()

    fun prepare() {
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
        state.value = ProductsState.Checkout
    }

    inner class GetProductsSubscriber : DisposableSingleObserver<List<Product>>() {

        override fun onSuccess(t: List<Product>) {
            Logger.getProductsCompleted()
            products = t

            when {
                products.isEmpty() -> state.value = ProductsState.Empty
                products.isNotEmpty() -> state.value =
                    ProductsState.Products(products.toPresentationModel())
            }
        }

        override fun onError(exception: Throwable) {
            Logger.getProductsError()
            state.value = ProductsState.Error
        }

    }

    inner class AddProductSubscriber : DisposableCompletableObserver() {

        override fun onComplete() {
            Logger.addProductCompleted()
        }

        override fun onError(e: Throwable) {
            Logger.addProductError()
            state.value = ProductsState.Error
        }

    }

    inner class GetCartSubscriber : DisposableSubscriber<Cart>() {

        override fun onComplete() {
            Logger.getCartCompleted()
        }

        override fun onNext(t: Cart) {
            Logger.getCartNext()
            state.value = ProductsState.Cart(t.items.size)
        }

        override fun onError(t: Throwable?) {
            Logger.getCartError()
            state.value = ProductsState.Error
        }
    }

}

interface ProductsListener {
    fun onProductClicked(code: String)

    fun onCheckoutClicked()
}