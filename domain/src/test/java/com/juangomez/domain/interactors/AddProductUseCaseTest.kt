package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddProductUseCaseTest {

    private lateinit var addProductUseCase: AddProductUseCase

    @Mock
    private lateinit var mockThreadExecutor: ThreadExecutor

    @Mock
    private lateinit var mockPostExecutionThread: PostExecutionThread

    @Mock
    private lateinit var mockCartRepository: CartRepository

    @Before
    fun setUp() {
        addProductUseCase = AddProductUseCase(
            mockCartRepository,
            mockThreadExecutor,
            mockPostExecutionThread
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

        stubCartRepositoryGetCart(Flowable.just(cart))
        stubCartRepositorySetCart(cart, Completable.complete())
        addProductUseCase.buildUseCaseCompletable(productToAdd)
            .test()
            .assertNoErrors()
            .assertComplete()

        verifyNumberOfInvocations(cart)

    }

    private fun stubCartRepositoryGetCart(single: Flowable<Cart>) {
        Mockito.`when`(mockCartRepository.getCart())
            .thenReturn(single)
    }

    private fun stubCartRepositorySetCart(cart: Cart, completable: Completable) {
        Mockito.`when`(mockCartRepository.setCart(cart))
            .thenReturn(completable)
    }

    private fun verifyNumberOfInvocations(cart: Cart) {
        verify(mockCartRepository, times(1)).getCart()
        verify(mockCartRepository, times(1)).setCart(cart)
    }

}