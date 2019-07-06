package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCartUseCaseTest {

    private lateinit var getCartUseCase: GetCartUseCase

    @Mock
    private lateinit var mockThreadExecutor: ThreadExecutor

    @Mock
    private lateinit var mockPostExecutionThread: PostExecutionThread

    @Mock
    private lateinit var mockCartRepository: CartRepository

    @Before
    fun setUp() {
        getCartUseCase = GetCartUseCase(
            mockCartRepository,
            mockThreadExecutor,
            mockPostExecutionThread
        )
    }

    @Test
    fun buildUseCaseObservableCallsRepository() {
        getCartUseCase.buildUseCaseFlowable(null)
        verify(mockCartRepository).getCart()
    }

    @Test
    fun buildUseCaseObservableCompletes() {
        val cart = Cart(mutableListOf())

        stubCartRepositoryGetCart(Flowable.just(cart))
        getCartUseCase.buildUseCaseFlowable(null)
            .test()
            .assertComplete()
    }

    @Test
    fun buildUseCaseObservableReturnsData() {
        val cart = Cart(
            mutableListOf(
                CartItem(
                    Product("VOUCHER", "Cabify Voucher", 5f)
                )
            )
        )

        stubCartRepositoryGetCart(Flowable.just(cart))
        getCartUseCase.buildUseCaseFlowable(null)
            .test()
            .assertValue(cart)
    }

    private fun stubCartRepositoryGetCart(single: Flowable<Cart>) {
        `when`(mockCartRepository.getCart())
            .thenReturn(single)
    }

}