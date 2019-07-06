package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.interactors.base.SingleUseCase
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.checkout.Checkout
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.models.product.Product
import io.reactivex.Completable
import io.reactivex.Single

open class CreateCheckoutUseCase constructor(
    val twoForOneOffer: TwoForOneOffer,
    val bulkOffer: BulkOffer,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    SingleUseCase<Checkout, Cart>(threadExecutor, postExecutionThread) {

    public override fun buildUseCaseSingle(params: Cart?): Single<Checkout> {
        val checkout = Checkout(params!!, twoForOneOffer, bulkOffer)
        return Single.just(checkout)
    }
}