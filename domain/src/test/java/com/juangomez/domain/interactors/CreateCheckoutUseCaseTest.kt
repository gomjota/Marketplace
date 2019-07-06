package com.juangomez.domain.interactors

import com.juangomez.domain.executor.PostExecutionThread
import com.juangomez.domain.executor.ThreadExecutor
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.models.product.Product
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
    private lateinit var mockThreadExecutor: ThreadExecutor

    @Mock
    private lateinit var mockPostExecutionThread: PostExecutionThread

    @Before
    fun setUp() {
        createCheckoutUseCase = CreateCheckoutUseCase(
            twoForOneOffer,
            bulkOffer,
            mockThreadExecutor,
            mockPostExecutionThread
        )
    }

    @Test
    fun buildUseCaseCompletes() {
        val cart = Cart(
            mutableListOf(
                CartItem(
                    Product("VOUCHER", "Cabify Voucher", 5f)
                )
            )
        )
        
        Mockito.`when`(twoForOneOffer.applyOffer(cart))
            .thenReturn(any())

        createCheckoutUseCase.buildUseCaseSingle(cart)
            .test()
            .assertNoErrors()
            .assertComplete()
    }

}