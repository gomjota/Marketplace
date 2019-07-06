package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.ProductRepository
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetProductsUseCaseTest {

    private lateinit var getProductsUseCase: GetProductsUseCase

    @Mock
    private lateinit var mockThreadExecutor: ThreadExecutor

    @Mock
    private lateinit var mockPostExecutionThread: PostExecutionThread

    @Mock
    private lateinit var mockProductsRepository: ProductRepository

    @Before
    fun setUp() {
        getProductsUseCase = GetProductsUseCase(
            mockProductsRepository,
            mockThreadExecutor,
            mockPostExecutionThread
        )
    }

    @Test
    fun buildUseCaseObservableCallsRepository() {
        getProductsUseCase.buildUseCaseFlowable(null)
        Mockito.verify(mockProductsRepository).getProducts()
    }

    @Test
    fun buildUseCaseObservableCompletes() {
        val products = emptyList<Product>()

        stubProductsRepositoryGetProducts(Flowable.just(products))
        getProductsUseCase.buildUseCaseFlowable(null)
            .test()
            .assertComplete()
    }

    @Test
    fun buildUseCaseObservableReturnsData() {
        val products = listOf(
            Product("VOUCHER", "Cabify Voucher", 5f),
            Product("TSHIRT", "Cabify Tshirt", 20f)
        )

        stubProductsRepositoryGetProducts(Flowable.just(products))
        getProductsUseCase.buildUseCaseFlowable(null)
            .test()
            .assertValue(products)
    }

    private fun stubProductsRepositoryGetProducts(single: Flowable<List<Product>>) {
        Mockito.`when`(mockProductsRepository.getProducts())
            .thenReturn(single)
    }

}