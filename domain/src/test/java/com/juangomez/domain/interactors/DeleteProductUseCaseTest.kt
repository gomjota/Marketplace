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
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeleteProductUseCaseTest {

    private lateinit var deleteProductUseCase: DeleteProductUseCase
    private lateinit var scope: CoroutineScope

    @Mock
    private lateinit var mockCartRepository: CartRepository

    @Before
    fun setUp() {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        deleteProductUseCase = DeleteProductUseCase(
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

        val productToDelete = Product("COPPER", "COPPER", 5f)

        runBlocking {
            stubCartRepositoryGetCart(cart)
            stubCartRepositoryDeleteCart()
            deleteProductUseCase.invoke(scope, DeleteProductUseCase.Params(productToDelete))

            verifyNumberOfInvocations()
        }

    }

    private suspend fun stubCartRepositoryGetCart(cart: Cart) {
        Mockito.`when`(mockCartRepository.getCart())
            .thenReturn(cart)
    }

    private suspend fun stubCartRepositoryDeleteCart() {
        Mockito.doNothing().`when`(mockCartRepository.deleteCart())
    }

    private suspend fun verifyNumberOfInvocations() {
        Mockito.verify(mockCartRepository, Mockito.times(1)).getCart()
        Mockito.verify(mockCartRepository, Mockito.times(1)).deleteCart()
    }

}