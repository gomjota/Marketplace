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
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeleteProductUseCaseTest {

    private lateinit var deleteProductUseCase: DeleteProductUseCase

    @Mock
    private lateinit var mockThreadExecutor: ThreadExecutor

    @Mock
    private lateinit var mockPostExecutionThread: PostExecutionThread

    @Mock
    private lateinit var mockCartRepository: CartRepository

    @Before
    fun setUp() {
        deleteProductUseCase = DeleteProductUseCase(
            mockCartRepository,
            mockThreadExecutor,
            mockPostExecutionThread
        )
    }

    @Test
    fun buildUseCaseObservableCompletes() {
        val cart = Cart(
            mutableListOf(
                CartItem(
                    Product("VOUCHER", "Cabify Voucher", 5f)
                )
            )
        )

        val productToDelete = Product("VOUCHER", "Cabify Voucher", 5f)

        stubCartRepositoryGetCart(Flowable.just(cart))
        stubCartRepositorySetCart(cart, Completable.complete())
        deleteProductUseCase.buildUseCaseCompletable(productToDelete)
            .test()
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
        Mockito.verify(mockCartRepository, Mockito.times(1)).getCart()
        Mockito.verify(mockCartRepository, Mockito.times(1)).setCart(cart)
    }

}