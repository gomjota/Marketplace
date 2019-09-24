package com.juangomez.presentation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
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


@RunWith(AndroidJUnit4::class)
class ProductsActivityTest {

    private val DEFAULT_LIST_SIZE = 3

    @RelaxedMockK
    lateinit var productsViewModel: ProductsViewModel

    private val productsToShow = MediatorLiveData<List<ProductPresentationModel>>()

    private val checkoutOpen = SingleLiveEvent<Void>()

    private val error = SingleLiveEvent<Void>()

    private val isLoading = MutableLiveData<Boolean>()

    private val isShowingEmptyCase = MutableLiveData<Boolean>()

    private val productsInCart = MutableLiveData<Int>()

    @Before
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

    @get:Rule
    var activityTestRule = ActivityTestRule(
        ProductsActivity::class.java, true,
        false
    )

    @get:Rule
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityTestRule)


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
    fun shouldShowList() {
        val productsList = generateProductsList(DEFAULT_LIST_SIZE)
        activityTestRule.launchActivity(null)
        productsToShow.postValue(productsList)
        
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.cart_group)).check(matches(not(isDisplayed())))
        onView(withId(R.id.empty_case)).check(matches(not(isDisplayed())))
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
}