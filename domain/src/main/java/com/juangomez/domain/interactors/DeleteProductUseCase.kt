package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import com.juangomez.domain.interactors.base.CompletableUseCase
import io.reactivex.Completable

open class DeleteProductUseCase constructor(
    val cartRepository: CartRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    CompletableUseCase<Product>(threadExecutor, postExecutionThread) {

    public override fun buildUseCaseCompletable(params: Product): Completable {
        return cartRepository.getCart()
            .map {
                it.removeProduct(params)
                it
            }.flatMapCompletable {
                cartRepository.setCart(it)
            }
    }

}