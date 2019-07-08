package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.interactors.base.CompletableUseCase
import com.juangomez.domain.repositories.CartRepository
import io.reactivex.Completable

open class DeleteCartUseCase constructor(
    val cartRepository: CartRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    CompletableUseCase<Void, Void?>(threadExecutor, postExecutionThread) {

    public override fun buildUseCaseCompletable(params: Void?): Completable {
        return cartRepository.deleteCart()
    }

}