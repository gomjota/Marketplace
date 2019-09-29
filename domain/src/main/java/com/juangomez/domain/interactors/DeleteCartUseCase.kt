package com.juangomez.domain.interactors

import com.juangomez.domain.interactors.base.BaseUseCase
import com.juangomez.domain.interactors.base.BaseUseCase.None
import com.juangomez.domain.models.base.Either
import com.juangomez.domain.models.base.Failure
import com.juangomez.domain.repositories.CartRepository


class DeleteCartUseCase(
    private val cartRepository: CartRepository
) : BaseUseCase<None, None>() {

    override suspend fun run(params: None?): Either<Failure, None> {
        return try {
            cartRepository.deleteCart()
            Either.Right(None())
        } catch (exp: Exception) {
            Either.Left(DeleteCartFailure(exp))
        }
    }

    data class DeleteCartFailure(val error: Exception) : Failure.FeatureFailure(error)
}