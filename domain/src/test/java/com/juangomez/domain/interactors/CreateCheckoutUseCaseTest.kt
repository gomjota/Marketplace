package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateCheckoutUseCaseTest {

    private lateinit var createCheckoutUseCase: CreateCheckoutUseCase

    @Mock
    private lateinit var twoForOneOffer: TwoForOneOffer

    @Mock
    private lateinit var bulkOffer: BulkOffer

    @Mock
    private lateinit var mockCartRepository: CartRepository

    @Mock
    private lateinit var mockThreadExecutor: ThreadExecutor

    @Mock
    private lateinit var mockPostExecutionThread: PostExecutionThread

    @Before
    fun setUp() {
        createCheckoutUseCase = CreateCheckoutUseCase(
            mockCartRepository,
            twoForOneOffer,
            bulkOffer,
            mockThreadExecutor,
            mockPostExecutionThread
        )
    }

    @Test
    fun buildUseCaseNoErrors() {
        val cart = Cart(
            mutableListOf(
                CartItem(
                    Product("VOUCHER", "Cabify Voucher", 5f)
                )
            )
        )

        Mockito.`when`(mockCartRepository.getCart())
            .thenReturn(Flowable.just(cart))

        Mockito.`when`(twoForOneOffer.applyOffer(cart))
            .thenReturn(cart)

        createCheckoutUseCase.buildUseCaseFlowable()
            .test()
            .assertNoErrors()

        Mockito.verify(mockCartRepository, Mockito.times(1)).getCart()
        Mockito.verify(twoForOneOffer, Mockito.times(1)).applyOffer(cart)

    }

}