package com.juangomez.marketplace.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.juangomez.domain.interactors.GetProductsUseCase
import com.juangomez.domain.models.product.Product
import com.juangomez.marketplace.models.ProductPresentationModel
import com.juangomez.marketplace.viewmodels.base.BaseViewModel
import io.reactivex.subscribers.DisposableSubscriber

class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : BaseViewModel(), ProductsListener {

    val isLoading = MutableLiveData<Boolean>()
    val isShowingEmptyCase = MutableLiveData<Boolean>()
    val products = MediatorLiveData<List<ProductPresentationModel>>()

    fun prepare() {
        isLoading.postValue(true)
        val disposable = GetProductsSubscriber()
        getProductsUseCase.execute(disposable)
        addDisposable(disposable)
    }

    override fun onProductClicked(code: String) {

    }

    inner class GetProductsSubscriber : DisposableSubscriber<List<Product>>() {

        override fun onComplete() {
            isLoading.postValue(false)
        }

        override fun onNext(t: List<Product>) {

        }

        override fun onError(exception: Throwable) {
            isLoading.postValue(false)
            isShowingEmptyCase.postValue(true)
        }

    }

}

interface ProductsListener {
    fun onProductClicked(code: String)
}