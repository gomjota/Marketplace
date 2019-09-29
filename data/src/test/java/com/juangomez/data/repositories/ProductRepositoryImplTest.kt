package com.juangomez.data.repositories

import com.juangomez.data.entities.ProductEntity
import com.juangomez.data.sources.remote.RemoteProductsSource
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.ProductRepository
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Exception

@RunWith(MockitoJUnitRunner::class)
class ProductRepositoryImplTest {

    private lateinit var productRepository: ProductRepository

    @Mock
    private lateinit var mockRemoteProductSource: RemoteProductsSource

    @Before
    fun setup() {
        productRepository = ProductRepositoryImpl(
            mockRemoteProductSource
        )
    }

    @Test
    fun `should get products from remote`() {
        val products = listOf(
            ProductEntity("COPPER", "COPPER", 5f)
        )

        Mockito.`when`(mockRemoteProductSource.getProducts())
            .thenReturn(Single.just(products))

        productRepository.getProducts()
            .test()
            .assertComplete()

        Mockito.verify(mockRemoteProductSource, Mockito.times(1)).getProducts()
    }
}