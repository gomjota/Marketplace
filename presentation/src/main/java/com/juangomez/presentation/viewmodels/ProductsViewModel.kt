package com.juangomez.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.juangomez.domain.interactors.GetProductsUseCase
import com.juangomez.domain.models.product.Product
import com.juangomez.presentation.mappers.toPresentationModel
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.viewmodels.base.BaseViewModel
import io.reactivex.observers.DisposableSingleObserver
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

    inner class GetProductsSubscriber : DisposableSingleObserver<List<Product>>() {
        override fun onSuccess(t: List<Product>) {
            isLoading.postValue(false)
            products.postValue(t.toPresentationModel())
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