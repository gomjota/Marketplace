package com.juangomez.data.repositories

import com.juangomez.data.entities.CartEntity
import com.juangomez.data.entities.ProductEntity
import com.juangomez.data.sources.persistence.DatabaseCartSource
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CartRepositoryImplTest {

    private lateinit var cartRepository: CartRepository

    @Mock
    private lateinit var mockDatabaseCartSource: DatabaseCartSource

    @Before
    fun setup() {
        cartRepository = CartRepositoryImpl(mockDatabaseCartSource)
    }

    @Test
    fun `should get cart and have the same quantity of items`() {
        val cartEntity = CartEntity(listOf(ProductEntity("VOUCHER", "Cabify Voucher", 5f)))

        stubDatabaseCartSourceGetCart(Flowable.just(cartEntity))

        cartRepository.getCart()
            .test()
            .assertNoErrors()
            .assertValue {
                it.items.size == cartEntity.products.size
            }
            .assertComplete()

        verifyNumberOfInvocationsWhenGetCart()
    }

    @Test
    fun `should insert cart`() {
        val cartEntity = CartEntity(listOf(ProductEntity("VOUCHER", "Cabify Voucher", 5f)))
        val cartItem = CartItem(product = Product("VOUCHER", "Cabify Voucher", 5f))
        val cart = Cart(mutableListOf(cartItem))
        stubDatabaseCartSourceInsertCart(cartEntity, Completable.complete())

        cartRepository.setCart(cart)
            .test()
            .assertNoErrors()
            .assertComplete()

        verifyNumberOfInvocationsWhenInsertCart(cartEntity)
    }

    private fun stubDatabaseCartSourceGetCart(single: Flowable<CartEntity>) {
        Mockito.`when`(mockDatabaseCartSource.getCart())
            .thenReturn(single)
    }

    private fun stubDatabaseCartSourceInsertCart(cartEntity: CartEntity, completable: Completable) {
        Mockito.`when`(mockDatabaseCartSource.insertCart(cartEntity))
            .thenReturn(completable)
    }

    private fun verifyNumberOfInvocationsWhenGetCart() {
        Mockito.verify(mockDatabaseCartSource, Mockito.times(1)).getCart()
    }

    private fun verifyNumberOfInvocationsWhenInsertCart(cartEntity: CartEntity) {
        Mockito.verify(mockDatabaseCartSource, Mockito.times(1)).insertCart(cartEntity)
    }

}