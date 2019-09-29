package com.juangomez.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.juangomez.domain.interactors.AddProductUseCase
import com.juangomez.domain.interactors.CreateCheckoutUseCase
import com.juangomez.domain.interactors.DeleteCartUseCase
import com.juangomez.domain.interactors.DeleteProductUseCase
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.checkout.Checkout
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.models.product.Product
import com.juangomez.presentation.common.SingleLiveEvent
import com.juangomez.presentation.mappers.toPresentationModel
import com.juangomez.presentation.models.CheckoutPresentationModel
import com.juangomez.presentation.util.any
import com.juangomez.presentation.util.mock
import com.juangomez.presentation.viewmodels.CheckoutViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.Spy


@RunWith(JUnit4::class)
class CheckoutViewModelTest {

    private val DEFAULT_LIST_SIZE = 10

    private lateinit var viewModel: CheckoutViewModel

    private lateinit var checkoutPrice: MutableLiveData<Float>
    private lateinit var paymentDone: SingleLiveEvent<Void>
    private lateinit var cartEmpty: SingleLiveEvent<Void>
    private lateinit var error: SingleLiveEvent<Void>
    private lateinit var checkoutProductsToShow: MediatorLiveData<List<CheckoutPresentationModel>>

    @Mock
    lateinit var addProductUseCase: AddProductUseCase

    @Mock
    lateinit var addProductDisposable: CheckoutViewModel.AddProductSubscriber

    @Mock
    lateinit var deleteProductUseCase: DeleteProductUseCase

    @Mock
    lateinit var deleteProductDisposable: CheckoutViewModel.DeleteProductSubscriber

    @Mock
    lateinit var deleteCartUseCase: DeleteCartUseCase

    @Mock
    lateinit var deleteCartDisposable: CheckoutViewModel.DeleteCartSubscriber

    @Mock
    lateinit var createCheckoutUseCase: CreateCheckoutUseCase

    @Mock
    lateinit var createCheckoutDisposable: CheckoutViewModel.CreateCheckoutSubscriber

    @Spy
    lateinit var twoForOneOffer: TwoForOneOffer

    @Spy
    lateinit var bulkOffer: BulkOffer

    @Mock
    lateinit var throwable: Throwable

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        viewModel = CheckoutViewModel(
            addProductUseCase,
            deleteProductUseCase,
            deleteCartUseCase,
            createCheckoutUseCase
        )

        checkoutPrice = viewModel.checkoutPrice
        paymentDone = viewModel.paymentDone
        cartEmpty = viewModel.cartEmpty
        error = viewModel.error
        checkoutProductsToShow = viewModel.checkoutProductsToShow

        addProductDisposable = viewModel.AddProductSubscriber()
        deleteProductDisposable = viewModel.DeleteProductSubscriber()
        deleteCartDisposable = viewModel.DeleteCartSubscriber()
        createCheckoutDisposable = viewModel.CreateCheckoutSubscriber()
    }

    @Test
    fun `should post value with cart when starts`() {
        val observer: Observer<List<CheckoutPresentationModel>> = mock()
        val checkout = generateCheckoutWithProducts(DEFAULT_LIST_SIZE)

        `when`(createCheckoutUseCase.execute(any(), eq(null))).thenAnswer {
            createCheckoutDisposable.onNext(
                checkout
            )
        }

        checkoutProductsToShow.observeForever(observer)
        viewModel.prepare()

        assert(checkoutProductsToShow.value!!.size == checkout.toPresentationModel().size)
    }

    @Test
    fun `should post value with checkout price when starts if cart is not empty`() {
        val observer: Observer<Float> = mock()
        val checkout = generateCheckoutWithProducts(DEFAULT_LIST_SIZE)

        `when`(createCheckoutUseCase.execute(any(), eq(null))).thenAnswer {
            createCheckoutDisposable.onNext(
                checkout
            )
        }

        checkoutPrice.observeForever(observer)
        viewModel.prepare()

        assert(checkoutPrice.value == checkout.checkoutCart.totalPrice)
    }

    @Test
    fun `should post value with cart empty when starts if cart is empty`() {
        val observer: Observer<Void> = mock()
        val checkout = generateCheckoutWithProducts(0)

        `when`(createCheckoutUseCase.execute(any(), eq(null))).thenAnswer {
            createCheckoutDisposable.onNext(
                checkout
            )
        }

        cartEmpty.observeForever(observer)
        viewModel.prepare()

        Mockito.verify(observer).onChanged(null)
    }

    @Test
    fun `should post value error if error creating checkout cart`() {
        val observer: Observer<Void> = mock()

        `when`(createCheckoutUseCase.execute(any(), eq(null))).thenAnswer {
            createCheckoutDisposable.onError(
                throwable
            )
        }

        error.observeForever(observer)
        viewModel.prepare()

        Mockito.verify(observer).onChanged(null)
    }

    @Test
    fun `should post value error if error adding product to cart`() {
        val observer: Observer<Void> = mock()
        val cart = generateCartWithProducts(DEFAULT_LIST_SIZE)
        val product: Product = cart.items.first().product

        viewModel.cart = cart

        `when`(addProductUseCase.execute(any(), eq(product))).thenAnswer {
            addProductDisposable.onError(
                throwable
            )
        }

        error.observeForever(observer)
        viewModel.onAddProductClicked(product.code)

        Mockito.verify(observer).onChanged(null)
    }

    @Test
    fun `should post value error if error deleting product to cart`() {
        val observer: Observer<Void> = mock()
        val cart = generateCartWithProducts(DEFAULT_LIST_SIZE)
        val product: Product = cart.items.first().product

        viewModel.cart = cart

        `when`(deleteProductUseCase.execute(any(), eq(product))).thenAnswer {
            deleteProductDisposable.onError(
                throwable
            )
        }

        error.observeForever(observer)
        viewModel.onDeleteProductClicked(product.code)

        Mockito.verify(observer).onChanged(null)
    }

    @Test
    fun `should post value payment done after delete cart when transaction finish`() {
        val observer: Observer<Void> = mock()

        `when`(deleteCartUseCase.execute(any(), eq(null))).thenAnswer {
            deleteCartDisposable.onComplete()
        }

        paymentDone.observeForever(observer)
        viewModel.onPayClicked()

        Mockito.verify(observer).onChanged(null)
    }

    @Test
    fun `should post value payment done error after delete cart when transaction finish`() {
        val observer: Observer<Void> = mock()

        `when`(deleteCartUseCase.execute(any(), eq(null))).thenAnswer {
            deleteCartDisposable.onError(throwable)
        }

        error.observeForever(observer)
        viewModel.onPayClicked()

        Mockito.verify(observer).onChanged(null)
    }

    private fun generateProductList(amount: Int): List<Product> {
        val defaultCode = "CODE"
        val defaultProduct = "PRODUCT"
        val defaultPrice = 100f

        if (amount > 0) {
            return (1..amount).map {
                Product(
                    "$defaultCode$it",
                    "$defaultProduct$it",
                    defaultPrice
                )
            }
        }

        return listOf()
    }

    private fun generateCartWithProducts(amountOfProducts: Int): Cart {
        val products = generateProductList(amountOfProducts)
        return Cart(products.map { CartItem(it) }.toMutableList())
    }

    private fun generateCheckoutWithProducts(amountOfProducts: Int): Checkout {
        val cart = generateCartWithProducts(amountOfProducts)
        return Checkout(cart, twoForOneOffer, bulkOffer)
    }
}