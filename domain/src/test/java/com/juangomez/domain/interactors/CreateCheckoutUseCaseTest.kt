package com.juangomez.domain.interactors

import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
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
    private lateinit var scope: CoroutineScope

    @Mock
    private lateinit var twoForOneOffer: TwoForOneOffer

    @Mock
    private lateinit var bulkOffer: BulkOffer

    @Mock
    private lateinit var mockCartRepository: CartRepository

    @Before
    fun setUp() {
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        createCheckoutUseCase = CreateCheckoutUseCase(
            mockCartRepository,
            twoForOneOffer,
            bulkOffer
        )
    }

    @Test
    fun buildUseCaseNoErrors() {
        val cart = Cart(
            mutableListOf(
                CartItem(
                    Product("COPPER", "COPPER", 5f)
                )
            )
        )

        runBlocking {
            Mockito.`when`(mockCartRepository.getCart())
                .thenReturn(cart)

            Mockito.`when`(twoForOneOffer.applyOffer(cart))
                .thenReturn(cart)

            createCheckoutUseCase.invoke(scope)

            Mockito.verify(mockCartRepository, Mockito.times(1)).getCart()
            Mockito.verify(twoForOneOffer, Mockito.times(1)).applyOffer(cart)

        }
    }

}