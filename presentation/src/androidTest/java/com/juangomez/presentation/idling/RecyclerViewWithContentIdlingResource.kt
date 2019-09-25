package com.juangomez.presentation.idling

import androidx.test.espresso.IdlingResource.ResourceCallback
import androidx.recyclerview.widget.RecyclerView
import android.app.Activity
import androidx.test.espresso.IdlingResource


class RecyclerViewWithContentIdlingResource(
    private val activity: Activity, private val recyclerViewId: Int,
    private val numberOfItems: Int
) : IdlingResource {

    override fun getName(): String {
        return "RecyclerViewWithContentIdlingResource"
    }

    override fun isIdleNow(): Boolean {
        val recyclerView = activity.findViewById(recyclerViewId) as RecyclerView
        val numberOfItemsInRecyclerView = recyclerView.adapter!!.itemCount
        return numberOfItemsInRecyclerView == numberOfItems
    }

    override fun registerIdleTransitionCallback(callback: ResourceCallback) {

    }
}