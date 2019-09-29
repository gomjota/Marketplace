package com.juangomez.presentation

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.product.Product
import com.juangomez.domain.repositories.CartRepository
import com.juangomez.domain.repositories.ProductRepository
import com.juangomez.presentation.idling.ViewVisibilityIdlingResource
import com.juangomez.presentation.mappers.toPresentationModel
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.recyclerview.RecyclerViewInteraction
import com.juangomez.presentation.recyclerview.matcher.RecyclerViewItemsCountMatcher.Companion.recyclerViewHasItemCount
import com.juangomez.presentation.rule.DataBindingIdlingResourceRule
import com.juangomez.presentation.views.ProductsActivity
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.*
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin


@RunWith(AndroidJUnit4::class)
class ProductsActivityTest {

    companion object {

        private const val DEFAULT_LIST_SIZE = 10
        private const val DEFAULT_DELAY = 10000L

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
        Intents.init()
    }

    @get:Rule
    var activityTestRule = ActivityTestRule(
        ProductsActivity::class.java, true,
        false
    )

    @get:Rule
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityTestRule)

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Test
    @Throws(InterruptedException::class)
    fun shouldShowEmptyCaseIfThereAreNoProducts() {
        givenThereAreNoProducts()

        startActivity()

        onView(withId(R.id.empty_case)).check(matches(isDisplayed()))
    }

    @Test
    @Throws(InterruptedException::class)
    fun shouldShowProductsIfThereAreProducts() {
        val products = givenThereAreDifferentProducts(DEFAULT_LIST_SIZE)
        val productsPresentation = products.toPresentationModel()

        startActivity()

        RecyclerViewInteraction.onRecyclerView<ProductPresentationModel>(withId(R.id.recycler_view))
            .withItems(productsPresentation)
            .check(object : RecyclerViewInteraction.ItemViewAssertion<ProductPresentationModel> {
                override fun check(
                    item: ProductPresentationModel,
                    view: View,
                    e: NoMatchingViewException?
                ) {
                    matches(hasDescendant(withText(item.name))).check(view, e)
                    matches(hasDescendant(withText(item.price))).check(view, e)
                }
            })
    }

    @Test
    @Throws(InterruptedException::class)
    fun shouldHideEmptyCaseIfThereAreProducts() {
        givenThereAreDifferentProducts(DEFAULT_LIST_SIZE)

        startActivity()

        onView(withId(R.id.empty_case)).check(matches(not(isDisplayed())))
    }

    @Test
    @Throws(InterruptedException::class)
    fun shouldHideLoadingIfThereAreProducts() {
        givenThereAreDifferentProducts(DEFAULT_LIST_SIZE)

        startActivity()

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
    }

    @Test
    @Throws(InterruptedException::class)
    fun shouldShowTheExactNumberOfProducts() {
        givenThereAreDifferentProducts(DEFAULT_LIST_SIZE)

        startActivity()

        onView(withId(R.id.recycler_view)).check(
            matches(recyclerViewHasItemCount(DEFAULT_LIST_SIZE))
        )
    }

    @Test
    @Throws(InterruptedException::class)
    fun shouldAddProductToCartOnRecyclerViewItemTapped() {
        val products = givenThereAreDifferentProducts(DEFAULT_LIST_SIZE)
        val productIndex = 0
        val cart = Cart(mutableListOf(CartItem(products[productIndex])))

        every { runBlocking { cartRepository.getCart() } } returns cart

        startActivity()

        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                productIndex,
                click()
            )
        )

        val cartGroup: Group = activityTestRule.activity.findViewById(R.id.cart_group)
        val idlingResource = ViewVisibilityIdlingResource(cartGroup, View.VISIBLE)

        IdlingRegistry.getInstance().register(idlingResource)
        onView(withId(R.id.cart_group)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @Test
    @Throws(InterruptedException::class)
    fun shouldShowCountOfItemsAddedToCart() {
        val productIndex = 0
        val cart = Cart(mutableListOf())

        every { runBlocking { cartRepository.getCart() } } returns cart

        startActivity()

        val textToShow =
            String.format(activityTestRule.activity.getString(R.string.cart_purchase), 1)

        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                productIndex,
                click()
            )
        )

        val cartGroup: Group = activityTestRule.activity.findViewById(R.id.cart_group)
        val idlingResource = ViewVisibilityIdlingResource(cartGroup, View.VISIBLE)

        IdlingRegistry.getInstance().register(idlingResource)
        onView(withId(R.id.cart_text)).check(matches(withText(textToShow)))
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @After
    fun tearDownAfter() {
        Intents.release()
    }

    private fun setupDefaultCartRepositoryMock() {
        every { runBlocking { cartRepository.getCart() } } returns Cart(mutableListOf())
        every { runBlocking { cartRepository.setCart(any()) } } just Runs
        every { runBlocking { cartRepository.deleteCart() } } just Runs
    }

    private fun startActivity() {
        activityTestRule.launchActivity(null)
    }

    private fun givenThereAreNoProducts(delayInMilliseconds: Long = 0) {
        every {
            runBlocking {
                productRepository.getProducts()
            }
        } returns emptyList()
    }

    private fun givenThereAreDifferentProducts(amount: Int): List<Product> {
        val defaultCode = "CODE"
        val defaultProduct = "PRODUCT"
        val defaultPrice = 100f

        val products = (1..amount).map {
            Product(
                "$defaultCode$it",
                "$defaultProduct$it",
                defaultPrice
            )
        }

        every { runBlocking { productRepository.getProducts() } } returns products

        return products
    }
}