package com.juangomez.presentation.views.adapters

import androidx.recyclerview.widget.RecyclerView
import com.juangomez.presentation.databinding.CheckoutRowBinding
import com.juangomez.presentation.databinding.ProductRowBinding
import com.juangomez.presentation.models.CheckoutPresentationModel
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.viewmodels.CheckoutListener

class CheckoutViewHolder(
    private val binding: CheckoutRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun render(checkout: CheckoutPresentationModel, listener: CheckoutListener) {
        binding.checkout = checkout
        binding.listener = listener
        binding.executePendingBindings()
    }
}