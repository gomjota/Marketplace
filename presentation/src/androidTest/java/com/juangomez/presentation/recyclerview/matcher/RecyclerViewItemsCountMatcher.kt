package com.juangomez.presentation.recyclerview.matcher

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher


class RecyclerViewItemsCountMatcher(private val expectedItemCount: Int) : BaseMatcher<View>() {

    override fun matches(item: Any): Boolean {
        val recyclerView = item as RecyclerView
        return recyclerView.adapter!!.itemCount == expectedItemCount
    }

    override fun describeTo(description: Description) {
        description.appendText("recycler view does not contains $expectedItemCount items")
    }

    companion object {

        fun recyclerViewHasItemCount(itemCount: Int): Matcher<View> {
            return RecyclerViewItemsCountMatcher(itemCount)
        }
    }
}
