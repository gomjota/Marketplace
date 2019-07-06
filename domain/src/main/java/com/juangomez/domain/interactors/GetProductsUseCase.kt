package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.ProductRepository
import com.juangomez.domain.interactors.base.FlowableUseCase
import com.juangomez.domain.interactors.base.SingleUseCase
import io.reactivex.Flowable
import io.reactivex.Single

open class GetProductsUseCase constructor(
    private val productRepository: ProductRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    SingleUseCase<List<Product>, Void?>(threadExecutor, postExecutionThread) {

    public override fun buildUseCaseSingle(params: Void?): Single<List<Product>> {
        return productRepository.getProducts()
    }

}