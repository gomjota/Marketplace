package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.interactors.base.FlowableUseCase
import com.juangomez.domain.interactors.base.SingleUseCase
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.checkout.Checkout
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

open class CreateCheckoutUseCase constructor(
    val cartRepository: CartRepository,
    val twoForOneOffer: TwoForOneOffer,
    val bulkOffer: BulkOffer,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    FlowableUseCase<Checkout, Void>(threadExecutor, postExecutionThread) {

    public override fun buildUseCaseFlowable(params: Void?): Flowable<Checkout> {
        return cartRepository.getCart()
            .flatMap {
                Flowable.just(Checkout(it, twoForOneOffer, bulkOffer))
        }
    }
}