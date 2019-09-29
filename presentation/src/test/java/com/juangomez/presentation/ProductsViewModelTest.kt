package com.juangomez.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.juangomez.domain.interactors.AddProductUseCase
import com.juangomez.domain.interactors.GetCartUseCase
import com.juangomez.domain.interactors.GetProductsUseCase
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.product.Product
import com.juangomez.presentation.common.SingleLiveEvent
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.util.any
import com.juangomez.presentation.util.mock
import com.juangomez.presentation.viewmodels.ProductsViewModel
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class ProductsViewModelTest {

    private val DEFAULT_LIST_SIZE = 10

    private lateinit var viewModel: ProductsViewModel

    private lateinit var isLoading: MutableLiveData<Boolean>
    private lateinit var isShowingEmptyCase: MutableLiveData<Boolean>
    private lateinit var productsInCart: MutableLiveData<Int>
    private lateinit var productsToShow: MediatorLiveData<List<ProductPresentationModel>>
    private lateinit var checkoutOpen: SingleLiveEvent<Void>
    private lateinit var error: SingleLiveEvent<Void>

    @Mock
    lateinit var getProductsUseCase: GetProductsUseCase

    lateinit var getProductsDisposable: ProductsViewModel.GetProductsSubscriber

    @Mock
    lateinit var addProductUseCase: AddProductUseCase

    @Mock
    lateinit var addProductDisposable: ProductsViewModel.AddProductSubscriber

    @Mock
    lateinit var getCartUseCase: GetCartUseCase

    @Mock
    lateinit var getCartDisposable: ProductsViewModel.GetCartSubscriber

    @Mock
    lateinit var throwable: Throwable

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        viewModel = ProductsViewModel(getProductsUseCase, addProductUseCase, getCartUseCase)

        isLoading = viewModel.isLoading
        isShowingEmptyCase = viewModel.isShowingEmptyCase
        productsInCart = viewModel.productsInCart
        productsToShow = viewModel.productsToShow
        checkoutOpen = viewModel.checkoutOpen
        error = viewModel.error

        getProductsDisposable = viewModel.GetProductsSubscriber()
        getCartDisposable = viewModel.GetCartSubscriber()
        addProductDisposable = viewModel.AddProductSubscriber()
    }

    @Test
    fun `should post value to start loading`() {
        val observer: Observer<Boolean> = mock()
        isLoading.observeForever(observer)
        viewModel.prepare()
        assertTrue(isLoading.value!!)
    }

    @Test
    fun `should post value to stop loading`() {
        val observer: Observer<Boolean> = mock()
        val products = generateEmptyProductList()

        `when`(getProductsUseCase.execute(any(), eq(null))).thenAnswer {
            getProductsDisposable.onSuccess(
                products
            )
        }

        isLoading.observeForever(observer)
        viewModel.prepare()

        assertFalse(isLoading.value!!)
    }

    @Test
    fun `should post value to show empty case if list is empty`() {
        val observer: Observer<Boolean> = mock()
        val products = generateEmptyProductList()

        `when`(getProductsUseCase.execute(any(), eq(null))).thenAnswer {
            getProductsDisposable.onSuccess(
                products
            )
        }

        isShowingEmptyCase.observeForever(observer)
        viewModel.prepare()

        assertTrue(isShowingEmptyCase.value!!)
    }

    @Test
    fun `should post value to stop loading if error getting products`() {
        val observer: Observer<Boolean> = mock()

        `when`(getProductsUseCase.execute(any(), eq(null))).thenAnswer {
            getProductsDisposable.onError(
                throwable
            )
        }

        isLoading.observeForever(observer)
        viewModel.prepare()

        assertFalse(isLoading.value!!)
    }

    @Test
    fun `should post value to show empty case if error getting products`() {
        val observer: Observer<Boolean> = mock()

        `when`(getProductsUseCase.execute(any(), eq(null))).thenAnswer {
            getProductsDisposable.onError(
                throwable
            )
        }

        isShowingEmptyCase.observeForever(observer)
        viewModel.prepare()

        assertTrue(isShowingEmptyCase.value!!)
    }

    @Test
    fun `should post value to show error if error getting products`() {
        val observer: Observer<Void> = mock()

        `when`(getProductsUseCase.execute(any(), eq(null))).thenAnswer {
            getProductsDisposable.onError(
                throwable
            )
        }

        error.observeForever(observer)
        viewModel.prepare()

        verify(observer).onChanged(null)
    }

    @Test
    fun `should post value with products to show products on screen`() {
        val observer: Observer<List<ProductPresentationModel>> = mock()
        val products = generateProductList(DEFAULT_LIST_SIZE)

        `when`(getProductsUseCase.execute(any(), eq(null))).thenAnswer {
            getProductsDisposable.onSuccess(
                products
            )
        }

        productsToShow.observeForever(observer)
        viewModel.prepare()

        assertTrue(productsToShow.value!!.size == products.size)
    }

    @Test
    fun `should post value with amount of products in cart`() {
        val observer: Observer<Int> = mock()
        val cart = generateCartWithProducts(DEFAULT_LIST_SIZE)

        `when`(getCartUseCase.execute(any(), eq(null))).thenAnswer {
            getCartDisposable.onNext(
                cart
            )
        }

        productsInCart.observeForever(observer)
        viewModel.initCartSubscriber()

        assertTrue(productsInCart.value!! == cart.items.size)
    }

    @Test
    fun `should post value with error after adding one product to cart`() {
        val observer: Observer<Void> = mock()
        val products = generateProductList(DEFAULT_LIST_SIZE)
        val productToAdd = products.first()

        viewModel.products = products

        `when`(addProductUseCase.execute(any(), eq(productToAdd))).thenAnswer {
            addProductDisposable.onError(Throwable())
        }

        error.observeForever(observer)
        viewModel.onProductClicked(productToAdd.code)


        verify(observer).onChanged(null)
    }

    private fun generateProductList(amount: Int): List<Product> {
        val defaultCode = "CODE"
        val defaultProduct = "PRODUCT"
        val defaultPrice = 100f

        return (1..amount).map {
            Product(
                "$defaultCode$it",
                "$defaultProduct$it",
                defaultPrice
            )
        }
    }

    private fun generateEmptyProductList(): List<Product> {
        return listOf()
    }

    private fun generateCartWithProducts(amountOfProducts: Int): Cart {
        val products = generateProductList(amountOfProducts)
        return Cart(products.map { CartItem(it) }.toMutableList())
    }
}