package com.juangomez.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.checkout.Checkout
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import com.juangomez.domain.repositories.ProductRepository
import com.juangomez.presentation.mappers.toPresentationModel
import com.juangomez.presentation.models.CheckoutPresentationModel
import com.juangomez.presentation.recyclerview.RecyclerViewInteraction
import com.juangomez.presentation.recyclerview.viewaction.ChildViewAction
import com.juangomez.presentation.views.CheckoutActivity
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Completable
import io.reactivex.Flowable
import org.hamcrest.Matchers.allOf
import org.junit.*
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin


@RunWith(AndroidJUnit4::class)
class CheckoutActivityTest {

    companion object {

        private const val DEFAULT_PRODUCT_COUNT = 10

        @MockK
        lateinit var productRepository: ProductRepository

        @MockK
        lateinit var cartRepository: CartRepository

        @BeforeClass
        @JvmStatic
        fun setup() {
            MockKAnnotations.init(this)

            loadKoinModules(module {
                single(override = true) {
                    productRepository
                }
                single(override = true) {
                    cartRepository
                }
            })
        }

        @AfterClass
        fun tearDown() {
            stopKoin()
        }
    }

    @Before
    fun setupBefore() {
        setupDefaultCartRepositoryMock()
    }

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(
        CheckoutActivity::class.java, true,
        false
    )

    @Test
    @Throws(InterruptedException::class)
    fun shouldShowCountOfProductsInCart() {
        val checkout = givenThereAreTheSameProductsInCartWithNoOffer(DEFAULT_PRODUCT_COUNT)
        val checkoutPresentation = checkout.toPresentationModel()

        startActivity()

        RecyclerViewInteraction.onRecyclerView<CheckoutPresentationModel>(withId(R.id.recycler_view))
            .withItems(checkoutPresentation)
            .check(object : RecyclerViewInteraction.ItemViewAssertion<CheckoutPresentationModel> {
                override fun check(
                    item: CheckoutPresentationModel,
                    view: View,
                    e: NoMatchingViewException?
                ) {
                    matches(
                        hasDescendant(
                            allOf(
                                withId(R.id.checkout_product_quantity),
                                withText("x$DEFAULT_PRODUCT_COUNT")
                            )
                        )
                    ).check(view, e)
                }
            })
    }

    @Test
    @Throws(InterruptedException::class)
    fun shouldAddTheSameProductShowingCountOfProductsInCart() {
        val checkout = givenThereAreTheSameProductsInCartWithNoOffer(DEFAULT_PRODUCT_COUNT)
        val productIndex = 0
        val productsAfterAddOne = DEFAULT_PRODUCT_COUNT + 1

        checkout.checkoutCart.items.add(checkout.checkoutCart.items.last())
        val checkoutPresentation = checkout.toPresentationModel()

        every { cartRepository.getCart() } answers { Flowable.just(checkout.checkoutCart) }

        startActivity()

        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                productIndex,
                ChildViewAction.clickChildViewWithId(R.id.checkout_product_add)
            )
        )

        RecyclerViewInteraction.onRecyclerView<CheckoutPresentationModel>(withId(R.id.recycler_view))
            .withItems(checkoutPresentation)
            .check(object : RecyclerViewInteraction.ItemViewAssertion<CheckoutPresentationModel> {
                override fun check(
                    item: CheckoutPresentationModel,
                    view: View,
                    e: NoMatchingViewException?
                ) {
                    matches(
                        hasDescendant(
                            allOf(
                                withId(R.id.checkout_product_quantity),
                                withText("x$productsAfterAddOne")
                            )
                        )
                    ).check(view, e)
                }
            })
    }

    @Test
    @Throws(InterruptedException::class)
    fun shouldDeleteTheSameProductShowingCountOfProductsInCart() {
        val checkout = givenThereAreTheSameProductsInCartWithNoOffer(DEFAULT_PRODUCT_COUNT)
        val productIndex = 0
        val productsAfterDeleteOne = DEFAULT_PRODUCT_COUNT - 1

        checkout.checkoutCart.items.removeAt(productIndex)
        val checkoutPresentation = checkout.toPresentationModel()

        every { cartRepository.getCart() } answers { Flowable.just(checkout.checkoutCart) }

        startActivity()

        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                productIndex,
                ChildViewAction.clickChildViewWithId(R.id.checkout_product_add)
            )
        )

        RecyclerViewInteraction.onRecyclerView<CheckoutPresentationModel>(withId(R.id.recycler_view))
            .withItems(checkoutPresentation)
            .check(object : RecyclerViewInteraction.ItemViewAssertion<CheckoutPresentationModel> {
                override fun check(
                    item: CheckoutPresentationModel,
                    view: View,
                    e: NoMatchingViewException?
                ) {
                    matches(
                        hasDescendant(
                            allOf(
                                withId(R.id.checkout_product_quantity),
                                withText("x$productsAfterDeleteOne")
                            )
                        )
                    ).check(view, e)
                }
            })
    }

    private fun setupDefaultCartRepositoryMock() {
        every { cartRepository.getCart() } answers { Flowable.just(Cart(mutableListOf())) }
        every { cartRepository.setCart(any()) } answers { Completable.complete() }
        every { cartRepository.deleteCart() } answers { Completable.complete() }
    }

    private fun startActivity() {
        activityTestRule.launchActivity(null)
    }

    private fun givenThereAreTheSameProductsInCartWithNoOffer(amount: Int): Checkout {
        val defaultCode = "CODE"
        val defaultProduct = "PRODUCT"
        val defaultPrice = 100f

        val cartItems = (1..amount).map {
            Product(
                defaultCode,
                defaultProduct,
                defaultPrice
            )
        }.map { CartItem(it) }.toMutableList()

        val cart = Cart(cartItems)
        every { cartRepository.getCart() } answers { Flowable.just(cart) }
        return Checkout(cart, TwoForOneOffer(), BulkOffer())
    }
}