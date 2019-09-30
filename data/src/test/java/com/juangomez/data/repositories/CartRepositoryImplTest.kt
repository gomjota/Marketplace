package com.juangomez.data.repositories

import com.juangomez.data.entities.CartEntity
import com.juangomez.data.entities.ProductEntity
import com.juangomez.data.sources.persistence.DatabaseCartSource
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import kotlinx.coroutines.runBlocking
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
        val cartEntity = CartEntity(listOf(ProductEntity("COPPER", "COPPER", 5f)))

        runBlocking {
            stubDatabaseCartSourceGetCart(cartEntity)
            val cart = cartRepository.getCart()
            assert(cart.items.size == cartEntity.products.size)
            verifyNumberOfInvocationsWhenGetCart()
        }

    }

    @Test
    fun `should insert cart`() {
        val cartEntity = CartEntity(listOf(ProductEntity("COPPER", "COPPER", 5f)))
        val cartItem = CartItem(product = Product("COPPER", "COPPER", 5f))
        val cart = Cart(mutableListOf(cartItem))

        runBlocking {
            stubDatabaseCartSourceInsertCart(cartEntity)
            cartRepository.setCart(cart)
            verifyNumberOfInvocationsWhenInsertCart(cartEntity)
        }
    }

    private suspend fun stubDatabaseCartSourceGetCart(cartEntity: CartEntity) {
        Mockito.`when`(mockDatabaseCartSource.getCart())
            .thenReturn(cartEntity)
    }

    private suspend fun stubDatabaseCartSourceInsertCart(cartEntity: CartEntity) {
        Mockito.doNothing().`when`(mockDatabaseCartSource.insertCart(cartEntity))
    }

    private suspend fun verifyNumberOfInvocationsWhenGetCart() {
        Mockito.verify(mockDatabaseCartSource, Mockito.times(1)).getCart()
    }

    private suspend fun verifyNumberOfInvocationsWhenInsertCart(cartEntity: CartEntity) {
        Mockito.verify(mockDatabaseCartSource, Mockito.times(1)).insertCart(cartEntity)
    }

}