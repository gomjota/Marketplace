package com.juangomez.domain.usecases

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import com.juangomez.domain.usecases.base.CompletableUseCase
import io.reactivex.Completable

open class AddProductUseCase constructor(
    val cartRepository: CartRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    CompletableUseCase<Product>(threadExecutor, postExecutionThread) {

    public override fun buildUseCaseObservable(params: Product): Completable {
        return cartRepository.getCart()
            .map {
                it.addProduct(params)
                it
            }.flatMapCompletable {
                cartRepository.setCart(it)
            }
    }

}