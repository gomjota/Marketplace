package com.juangomez.domain.interactors

import com.juangomez.domain.interactors.base.BaseUseCase
import com.juangomez.domain.interactors.base.BaseUseCase.None
import com.juangomez.domain.models.base.Either
import com.juangomez.domain.models.base.Failure
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository


class DeleteProductUseCase(
    private val cartRepository: CartRepository
) : BaseUseCase<None, DeleteProductUseCase.Params>() {

    override suspend fun run(params: Params?): Either<Failure, None> {
        return try {
            val cartWithProductDeleted = cartRepository.getCart().removeProduct(params!!.product)

            if (cartWithProductDeleted.items.isEmpty()) {
                cartRepository.deleteCart()
            } else {
                cartRepository.setCart(cartWithProductDeleted)
            }

            Either.Right(None())
        } catch (exp: Exception) {
            Either.Left(DeleteProductFailure(exp))
        }
    }

    data class Params(val product: Product)

    data class DeleteProductFailure(val error: Exception) : Failure.FeatureFailure(error)
}