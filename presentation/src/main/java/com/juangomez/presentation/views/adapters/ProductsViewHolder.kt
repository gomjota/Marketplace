package com.juangomez.presentation.views.adapters

import androidx.recyclerview.widget.RecyclerView
import com.juangomez.presentation.databinding.ProductRowBinding
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.viewmodels.ProductsListener

class ProductsViewHolder(
    private val binding: ProductRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun render(product: ProductPresentationModel, listener: ProductsListener) {
        binding.product = product
        binding.listener = listener
        binding.executePendingBindings()
    }
}