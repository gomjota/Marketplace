package com.juangomez.presentation

import android.view.View
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.juangomez.domain.models.cart.Cart
import com.juangomez.domain.models.cart.CartItem
import com.juangomez.domain.models.checkout.Checkout
import com.juangomez.domain.models.offer.BulkOffer
import com.juangomez.domain.models.offer.TwoForOneOffer
import com.juangomez.domain.models.product.Product
import com.juangomez.presentation.common.SingleLiveEvent
import com.juangomez.presentation.idling.DataBindingIdlingResourceRule
import com.juangomez.presentation.mappers.toPresentationModel
import com.juangomez.presentation.models.CheckoutPresentationModel
import com.juangomez.presentation.recyclerview.RecyclerViewInteraction
import com.juangomez.presentation.recyclerview.viewaction.ChildViewAction
import com.juangomez.presentation.viewmodels.CheckoutViewModel
import com.juangomez.presentation.views.CheckoutActivity
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import junit.framework.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class CheckoutActivityTest {

    companion object {

        @RelaxedMockK
        lateinit var checkoutViewModel: CheckoutViewModel

        private val checkoutProductsToShow = MediatorLiveData<List<CheckoutPresentationModel>>()
        private val paymentDone = SingleLiveEvent<Void>()
        private val cartEmpty = SingleLiveEvent<Void>()
        private val error = SingleLiveEvent<Void>()
        private val checkoutPrice = MutableLiveData<Float>()

        @BeforeClass
        @JvmStatic
        fun setup() {
            MockKAnnotations.init(this)
            StandAloneContext.loadKoinModules(module {
                viewModel {
                    checkoutViewModel
                }
            })

            every { checkoutViewModel.checkoutProductsToShow } returns checkoutProductsToShow
            every { checkoutViewModel.paymentDone } returns paymentDone
            every { checkoutViewModel.error } returns error
            every { checkoutViewModel.cartEmpty } returns cartEmpty
            every { checkoutViewModel.checkoutPrice } returns checkoutPrice
            every { checkoutViewModel.prepare() } just Runs
        }
    }

    @get:Rule
    var activityTestRule = ActivityTestRule(
        CheckoutActivity::class.java, true,
        false
    )

    @get:Rule
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityTestRule)

    @get:Rule
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    @Test
    @Throws(InterruptedException::class)
    fun shouldMatchList() {
        activityTestRule.launchActivity(null)

        val checkout = generateBaseCheckout()
        val checkoutPresentationModel = checkout.toPresentationModel()

        checkoutProductsToShow.postValue(checkoutPresentationModel)
        drain()

        RecyclerViewInteraction.onRecyclerView<CheckoutPresentationModel>(withId(R.id.recycler_view))
            .withItems(checkoutPresentationModel)
            .check(object : RecyclerViewInteraction.ItemViewAssertion<CheckoutPresentationModel> {
                override fun check(
                    item: CheckoutPresentationModel,
                    view: View,
                    e: NoMatchingViewException?
                ) {
                    ViewAssertions.matches(hasDescendant(withText(item.name)))
                        .check(view, e)
                    ViewAssertions.matches(hasDescendant(withText(item.price)))
                        .check(view, e)
                }
            })
    }

    @Test
    @Throws(InterruptedException::class)
    fun shouldFinishIfCartEmpty() {
        activityTestRule.launchActivity(null)

        cartEmpty.postValue(null)
        drain()

        assertTrue(activityTestRule.activity.isFinishing)
    }

    private fun generateBaseCheckout(): Checkout {
        val defaultCode = "VOUCHER"
        val defaultName = "Voucher"
        val defaultPrice = 100f

        val product = Product(defaultCode, defaultName, defaultPrice)
        val cartItem = CartItem(product)
        val cart = Cart(mutableListOf(cartItem))
        return Checkout(cart, TwoForOneOffer(), BulkOffer())
    }

    private fun drain() {
        countingTaskExecutorRule.drainTasks(5, TimeUnit.SECONDS)
    }
}