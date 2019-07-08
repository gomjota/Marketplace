package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.ProductRepository
import io.reactivex.Flowable
import io.reactivex.Single
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
    fun buildUseCaseCallsRepository() {
        getProductsUseCase.buildUseCaseSingle(null)
        Mockito.verify(mockProductsRepository).getProducts()
    }

    @Test
    fun buildUseCaseCompletes() {
        val products = emptyList<Product>()

        stubProductsRepositoryGetProducts(Single.just(products))
        getProductsUseCase.buildUseCaseSingle(null)
            .test()
            .assertComplete()
    }

    @Test
    fun buildUseCaseReturnsData() {
        val products = listOf(
            Product("VOUCHER", "Cabify Voucher", 5f),
            Product("TSHIRT", "Cabify Tshirt", 20f)
        )

        stubProductsRepositoryGetProducts(Single.just(products))
        getProductsUseCase.buildUseCaseSingle(null)
            .test()
            .assertValue(products)
    }

    private fun stubProductsRepositoryGetProducts(single: Single<List<Product>>) {
        Mockito.`when`(mockProductsRepository.getProducts())
            .thenReturn(single)
    }

}