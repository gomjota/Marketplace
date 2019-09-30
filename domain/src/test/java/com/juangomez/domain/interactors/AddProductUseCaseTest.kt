package com.juangomez.domain.interactors

import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddProductUseCaseTest {

    private lateinit var addProductUseCase: AddProductUseCase
    private lateinit var scope: CoroutineScope

    @Mock
    private lateinit var mockCartRepository: CartRepository

    @Before
    fun setUp() {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        addProductUseCase = AddProductUseCase(
            mockCartRepository
        )
    }

    @Test
    fun buildUseCaseCompletes() {
        val cart = Cart(
            mutableListOf(
                CartItem(
                    Product("COPPER", "COPPER", 5f)
                )
            )
        )

        val productToAdd = Product("COPPER", "COPPER", 5f)

        runBlocking {
            stubCartRepositoryGetCart(cart)
            stubCartRepositorySetCart(cart)
            addProductUseCase.invoke(scope, AddProductUseCase.Params(productToAdd))

            verifyNumberOfInvocations(cart)
        }
    }

    private suspend fun stubCartRepositoryGetCart(cart: Cart) {
        `when`(mockCartRepository.getCart())
            .thenReturn(cart)
    }

    private suspend fun stubCartRepositorySetCart(cart: Cart) {
        doNothing().`when`(mockCartRepository.setCart(cart))
    }

    private suspend fun verifyNumberOfInvocations(cart: Cart) {
        verify(mockCartRepository, times(1)).getCart()
        verify(mockCartRepository, times(1)).setCart(cart)
    }

}