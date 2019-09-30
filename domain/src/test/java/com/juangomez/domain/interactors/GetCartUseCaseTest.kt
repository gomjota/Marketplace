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
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCartUseCaseTest {

    private lateinit var getCartUseCase: GetCartUseCase
    private lateinit var scope: CoroutineScope

    @Mock
    private lateinit var mockCartRepository: CartRepository

    @Before
    fun setUp() {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        getCartUseCase = GetCartUseCase(
            mockCartRepository
        )
    }

    @Test
    fun buildUseCaseCallsRepository() {
        runBlocking {
            getCartUseCase.invoke(scope)
            verify(mockCartRepository).getCart()
        }
    }

    private suspend fun stubCartRepositoryGetCart(cart: Cart) {
        `when`(mockCartRepository.getCart())
            .thenReturn(cart)
    }

}