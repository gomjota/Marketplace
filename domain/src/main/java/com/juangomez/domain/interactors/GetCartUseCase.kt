package com.juangomez.domain.interactors

import com.juangomez.domain.interactors.base.BaseUseCase
import com.juangomez.domain.interactors.base.BaseUseCase.None
import com.juangomez.domain.models.base.Either
import com.juangomez.domain.models.base.Failure
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.repositories.CartRepository

class GetCartUseCase(
    private val cartRepository: CartRepository
) : BaseUseCase<Cart, None>() {

    override suspend fun run(params: None?): Either<Failure, Cart> {
        return try {
            val cart = cartRepository.getCart()
            Either.Right(cart)
        } catch (exp: Exception) {
            Either.Left(GetCartFailure(exp))
        }
    }

    data class GetCartFailure(val error: Exception) : Failure.FeatureFailure(error)
}