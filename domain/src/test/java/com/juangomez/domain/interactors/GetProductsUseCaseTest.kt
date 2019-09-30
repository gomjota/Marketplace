package com.juangomez.domain.interactors

import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetProductsUseCaseTest {

    private lateinit var getProductsUseCase: GetProductsUseCase
    private lateinit var scope: CoroutineScope

    @Mock
    private lateinit var mockProductsRepository: ProductRepository

    @Before
    fun setUp() {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        getProductsUseCase = GetProductsUseCase(
            mockProductsRepository
        )
    }

    /*@Test
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
            Product("COPPER", "COPPER", 5f),
            Product("COMMANDER2", "COMMANDER2", 20f)
        )

        stubProductsRepositoryGetProducts(Single.just(products))
        getProductsUseCase.buildUseCaseSingle(null)
            .test()
            .assertValue(products)
    }

    private fun stubProductsRepositoryGetProducts(single: Single<List<Product>>) {
        Mockito.`when`(mockProductsRepository.getProducts())
            .thenReturn(single)
    }*/

}