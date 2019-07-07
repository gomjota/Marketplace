package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import com.juangomez.domain.interactors.base.CompletableUseCase
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import io.reactivex.Completable

open class DeleteProductUseCase constructor(
    val cartRepository: CartRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    CompletableUseCase<Void, Product?>(threadExecutor, postExecutionThread) {

    public override fun buildUseCaseCompletable(params: Product?): Completable {
        return cartRepository.getCart()
            .map {
                it.removeProduct(params!!)
            }.flatMapCompletable {
                cartRepository.setCart(it)
            }
    }

}