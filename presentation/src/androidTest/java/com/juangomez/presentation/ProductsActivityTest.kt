package com.juangomez.presentation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.juangomez.presentation.common.SingleLiveEvent
import com.juangomez.presentation.idling.DataBindingIdlingResourceRule
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.viewmodels.ProductsViewModel
import com.juangomez.presentation.views.ProductsActivity
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import org.hamcrest.Matchers.not
import org.junit.*
import org.junit.runner.RunWith
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import androidx.test.espresso.NoMatchingViewException
import com.juangomez.presentation.recyclerview.RecyclerViewInteraction
import android.view.View
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class ProductsActivityTest {

    companion object {

        private const val DEFAULT_LIST_SIZE = 10

        @RelaxedMockK
        lateinit var productsViewModel: ProductsViewModel

        private val productsToShow = MediatorLiveData<List<ProductPresentationModel>>()

        private val checkoutOpen = SingleLiveEvent<Void>()

        private val error = SingleLiveEvent<Void>()

        private val isLoading = MutableLiveData<Boolean>()

        private val isShowingEmptyCase = MutableLiveData<Boolean>()

        private val productsInCart = MutableLiveData<Int>()

        @BeforeClass
        @JvmStatic
        fun setup() {
            MockKAnnotations.init(this)
            loadKoinModules(module {
                viewModel {
                    productsViewModel
                }
            })

            every { productsViewModel.productsToShow } returns productsToShow
            every { productsViewModel.checkoutOpen } returns checkoutOpen
            every { productsViewModel.error } returns error
            every { productsViewModel.isLoading } returns isLoading
            every { productsViewModel.isShowingEmptyCase } returns isShowingEmptyCase
            every { productsViewModel.productsInCart } returns productsInCart
            every { productsViewModel.prepare() } answers { isLoading.postValue(true) }
            every { productsViewModel.initCartSubscriber() } just Runs
        }
    }

    @get:Rule
    var activityTestRule = ActivityTestRule(
        ProductsActivity::class.java, true,
        false
    )

    @get:Rule
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityTestRule)

    @get:Rule
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    @Test
    @Throws(InterruptedException::class)
    fun onlyLoadingShouldBeVisibleAtLaunch() {
        activityTestRule.launchActivity(null)
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.cart_group)).check(matches(not(isDisplayed())))
        onView(withId(R.id.empty_case)).check(matches(not(isDisplayed())))
    }

    @Test
    @Throws(InterruptedException::class)
    fun shouldMatchList() {
        val productsList = generateProductsList(DEFAULT_LIST_SIZE)
        activityTestRule.launchActivity(null)

        productsToShow.postValue(productsList)
        drain()

        isLoading.postValue(false)
        drain()

        RecyclerViewInteraction.onRecyclerView<ProductPresentationModel>(withId(R.id.recycler_view))
            .withItems(productsList)
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
    fun shouldShowCartWhenProductIsClicked() {
        val productsList = generateProductsList(DEFAULT_LIST_SIZE)
        val productIndex = 0
        activityTestRule.launchActivity(null)

        productsToShow.postValue(productsList)
        drain()

        isLoading.postValue(false)
        drain()

        every { productsViewModel.onProductClicked(productsList[productIndex].code) } answers {
            productsInCart.postValue(
                1
            )
        }

        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                productIndex,
                click()
            )
        )

        drain()

        onView(withId(R.id.cart_view)).check(matches(isDisplayed()))
    }

    private fun generateProductsList(amount: Int): List<ProductPresentationModel> {
        val defaultCode = "CODE"
        val defaultProduct = "PRODUCT"
        val defaultPrice = 100

        return (1..amount).map {
            ProductPresentationModel(
                "$defaultCode$it",
                "$defaultProduct$it",
                (defaultPrice + it).toString()
            )
        }
    }

    private fun drain() {
        countingTaskExecutorRule.drainTasks(5, TimeUnit.SECONDS)
    }
}