package com.juangomez.domain.interactors

import com.juangomez.domain.interactors.base.BaseUseCase
import com.juangomez.domain.interactors.base.BaseUseCase.None
import com.juangomez.domain.models.base.Either
import com.juangomez.domain.models.base.Failure
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository

class AddProductUseCase(
    private val cartRepository: CartRepository
) : BaseUseCase<None, AddProductUseCase.Params>() {

    override suspend fun run(params: Params?): Either<Failure, None> {
        return try {
            val cartWithProductAdded = cartRepository.getCart().addProduct(params!!.product)
            cartRepository.setCart(cartWithProductAdded)

            Either.Right(None())
        } catch (exp: Exception) {
            Either.Left(AddProductFailure(exp))
        }
    }

    data class Params(val product: Product)

    data class AddProductFailure(val error: Exception) : Failure.FeatureFailure(error)
}