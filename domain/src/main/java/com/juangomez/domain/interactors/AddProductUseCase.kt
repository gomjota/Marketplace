package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import com.juangomez.domain.interactors.base.CompletableUseCase
import com.juangomez.domain.mappers.toCartItemModel
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import io.reactivex.Completable

open class AddProductUseCase constructor(
    val cartRepository: CartRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    CompletableUseCase<Void, Product?>(threadExecutor, postExecutionThread) {

    public override fun buildUseCaseCompletable(params: Product?): Completable {
        return cartRepository.getCart()
            .take(1)
            .map {
                it.addProduct(params!!)
            }
            .flatMapCompletable {
                cartRepository.setCart(it)
            }
    }

}