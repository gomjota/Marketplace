package com.juangomez.presentation.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.juangomez.presentation.R
import com.juangomez.presentation.databinding.CheckoutRowBinding
import com.juangomez.presentation.databinding.ProductRowBinding
import com.juangomez.presentation.models.CheckoutPresentationModel
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.viewmodels.CheckoutViewModel

internal class CheckoutAdapter(
    private val viewModel: CheckoutViewModel
) : RecyclerView.Adapter<CheckoutViewHolder>() {
    private val checkout: MutableList<CheckoutPresentationModel> = ArrayList()

    fun addAll(collection: Collection<CheckoutPresentationModel>) {
        checkout.addAll(collection)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val binding: CheckoutRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.checkout_row,
            parent,
            false
        )

        return CheckoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        val product = checkout[position]
        holder.render(product, viewModel)
    }

    override fun getItemCount(): Int {
        return checkout.size
    }

    fun clear() {
        checkout.clear()
    }
}