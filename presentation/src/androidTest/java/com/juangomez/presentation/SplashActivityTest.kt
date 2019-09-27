package com.juangomez.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.juangomez.presentation.views.SplashActivity
import androidx.test.rule.ActivityTestRule
import com.juangomez.presentation.idling.ElapsedTimeIdlingResource
import com.juangomez.presentation.rule.DataBindingIdlingResourceRule
import com.juangomez.presentation.rule.RxSchedulerRule
import com.juangomez.presentation.views.ProductsActivity
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(
        SplashActivity::class.java, true,
        false
    )

    @get:Rule
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule(activityTestRule)

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    @Throws(InterruptedException::class)
    fun shouldKeepSplashThenNavigateToProducts() {
        activityTestRule.launchActivity(null)
        val idlingResource = ElapsedTimeIdlingResource(BuildConfig.DEFAULT_SPLASH_TIME)

        IdlingRegistry.getInstance().register(idlingResource)
        intended(hasComponent(ProductsActivity::class.java.name))
        IdlingRegistry.getInstance().unregister(idlingResource)
    }

    @After
    fun tearDown() {
        Intents.release()
    }
}