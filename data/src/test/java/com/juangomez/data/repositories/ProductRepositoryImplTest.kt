package com.juangomez.data.repositories

import com.juangomez.data.entities.ProductEntity
import com.juangomez.data.sources.database.DatabaseProductsSource
import com.juangomez.data.sources.remote.RemoteProductsSource
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.ProductRepository
import io.reactivex.Completable
import io.reactivex.Flowable
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
    private lateinit var mockDatabaseProductSource: DatabaseProductsSource

    @Mock
    private lateinit var mockRemoteProductSource: RemoteProductsSource

    @Before
    fun setup() {
        productRepository = ProductRepositoryImpl(
            mockRemoteProductSource,
            mockDatabaseProductSource
        )
    }

    @Test
    fun `should get products from remote if database is empty`() {
        val products = listOf(
            ProductEntity("VOUCHER", "Cabify Voucher", 5f)
        )

        Mockito.`when`(mockDatabaseProductSource.getProducts())
            .thenReturn(Flowable.error(Exception()))

        Mockito.`when`(mockRemoteProductSource.getProducts())
            .thenReturn(Single.just(products))

        productRepository.getProducts()
            .test()
            .assertNotComplete()

        Mockito.verify(mockDatabaseProductSource, Mockito.times(1)).getProducts()
        Mockito.verify(mockRemoteProductSource, Mockito.times(1)).getProducts()
    }

    @Test
    fun `should get products from database if database have data`() {
        val products = listOf(
            ProductEntity("VOUCHER", "Cabify Voucher", 5f)
        )

        Mockito.`when`(mockDatabaseProductSource.getProducts())
            .thenReturn(Flowable.just(products))

        Mockito.`when`(mockRemoteProductSource.getProducts())
            .thenReturn(Single.just(products))

        productRepository.getProducts()
            .test()
            .assertNoErrors()
            .assertComplete()

        Mockito.verify(mockDatabaseProductSource, Mockito.times(1)).getProducts()
        Mockito.verify(mockRemoteProductSource, Mockito.times(0)).getProducts()
    }

    @Test
    fun `should insert product in database`() {
        val products = listOf(
            Product("VOUCHER", "Cabify Voucher", 5f)
        )

        val productEntities = listOf(
            ProductEntity("VOUCHER", "Cabify Voucher", 5f)
        )

        Mockito.`when`(mockDatabaseProductSource.insertProducts(productEntities))
            .thenReturn(Completable.complete())

        productRepository.setProducts(products)
            .test()
            .assertNoErrors()
            .assertComplete()

        Mockito.verify(mockDatabaseProductSource, Mockito.times(1))
            .insertProducts(productEntities)
    }

}