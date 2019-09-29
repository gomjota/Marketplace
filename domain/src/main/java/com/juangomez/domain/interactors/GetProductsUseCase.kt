package com.juangomez.domain.interactors

import com.juangomez.domain.interactors.base.BaseUseCase
import com.juangomez.domain.interactors.base.BaseUseCase.None
import com.juangomez.domain.models.base.Either
import com.juangomez.domain.models.base.Failure
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.ProductRepository

class GetProductsUseCase(
    private val productRepository: ProductRepository
) : BaseUseCase<List<Product>, None>() {

    override suspend fun run(params: None?): Either<Failure, List<Product>> {
        return try {
            val products = productRepository.getProducts()
            Either.Right(products)
        } catch (exp: Exception) {
            Either.Left(GetProductsFailure(exp))
        }
    }

    data class GetProductsFailure(val error: Exception) : Failure.FeatureFailure(error)
}