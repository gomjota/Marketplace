package com.juangomez.domain.usecases

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.repositories.CartRepository
import com.juangomez.domain.usecases.base.FlowableUseCase
import io.reactivex.Flowable

open class GetCartUseCase constructor(
    val cartRepository: CartRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
):
    FlowableUseCase<Cart, Void?>(threadExecutor, postExecutionThread) {

    public override fun buildUseCaseObservable(params: Void?): Flowable<Cart> {
        return cartRepository.getCart()
    }

}