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
    fun buildUseCaseCompletes() {
        val cart = Cart(
            mutableListOf(
                CartItem(
                    Product("COPPER", "COPPER", 5f)
                )
            )
        )

        val productToDelete = Product("COPPER", "COPPER", 5f)

        stubCartRepositoryGetCart(cart)
        stubCartRepositoryDeleteCart(Completable.complete())
        deleteProductUseCase.buildUseCaseCompletable(productToDelete)
            .test()
            .assertNoErrors()
            .assertComplete()

        verifyNumberOfInvocations()
    }

    private fun stubCartRepositoryGetCart(cart: Cart) {
        Mockito.`when`(mockCartRepository.getCart())
            .thenReturn(Flowable.just(cart))
    }

    private fun stubCartRepositoryDeleteCart(completable: Completable) {
        Mockito.`when`(mockCartRepository.deleteCart())
            .thenReturn(completable)
    }

    private fun verifyNumberOfInvocations() {
        Mockito.verify(mockCartRepository, Mockito.times(1)).getCart()
        Mockito.verify(mockCartRepository, Mockito.times(1)).deleteCart()
    }

}