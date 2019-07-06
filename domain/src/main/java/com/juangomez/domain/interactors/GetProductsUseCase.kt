package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.ProductRepository
import com.juangomez.domain.interactors.base.FlowableUseCase
import io.reactivex.Flowable

open class GetProductsUseCase constructor(
    val productRepository: ProductRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    FlowableUseCase<List<Product>, Void?>(threadExecutor, postExecutionThread) {

    public override fun buildUseCaseFlowable(params: Void?): Flowable<List<Product>> {
        return productRepository.getProducts()
    }

}