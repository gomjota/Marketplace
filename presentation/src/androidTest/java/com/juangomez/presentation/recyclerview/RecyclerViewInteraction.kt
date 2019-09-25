package com.juangomez.presentation.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import org.hamcrest.Matcher


class RecyclerViewInteraction<A> private constructor(private val viewMatcher: Matcher<View>) {
    private var items: List<A>? = null

    fun withItems(items: List<A>): RecyclerViewInteraction<A> {
        this.items = items
        return this
    }

    fun check(itemViewAssertion: ItemViewAssertion<A>): RecyclerViewInteraction<A> {
        for (i in items!!.indices) {
            onView(viewMatcher)
                .perform(scrollToPosition<RecyclerView.ViewHolder>(i))
                .check(RecyclerItemViewAssertion(i, items!![i], itemViewAssertion))
        }
        return this
    }

    interface ItemViewAssertion<A> {
        fun check(item: A, view: View, e: NoMatchingViewException?)
    }

    companion object {

        fun <A> onRecyclerView(viewMatcher: Matcher<View>): RecyclerViewInteraction<A> {
            return RecyclerViewInteraction(viewMatcher)
        }
    }
}