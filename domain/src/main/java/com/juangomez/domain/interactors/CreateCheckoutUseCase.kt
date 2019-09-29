package com.juangomez.domain.interactors

import com.juangomez.domain.interactors.base.BaseUseCase
import com.juangomez.domain.interactors.base.BaseUseCase.None
import com.juangomez.domain.models.base.Either
import com.juangomez.domain.models.base.Failure
import com.juangomez.domain.models.checkout.Checkout
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.repositories.CartRepository

class CreateCheckoutUseCase(
    private val cartRepository: CartRepository,
    private val twoForOneOffer: TwoForOneOffer,
    private val bulkOffer: BulkOffer
) : BaseUseCase<Checkout, None?>() {

    override suspend fun run(params: None?): Either<Failure, Checkout> {
        return try {
            val cart = cartRepository.getCart()
            val checkout = Checkout(cart, twoForOneOffer, bulkOffer)

            Either.Right(checkout)
        } catch (exp: Exception) {
            Either.Left(AddProductFailure(exp))
        }
    }

    data class AddProductFailure(val error: Exception) : Failure.FeatureFailure(error)
}