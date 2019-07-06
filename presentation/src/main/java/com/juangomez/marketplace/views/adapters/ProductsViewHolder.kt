package com.juangomez.marketplace.views.adapters

import androidx.recyclerview.widget.RecyclerView
import com.juangomez.marketplace.databinding.ProductRowBinding
import com.juangomez.marketplace.models.ProductPresentationModel
import com.juangomez.marketplace.viewmodels.ProductsListener

class ProductsViewHolder(
    private val binding: ProductRowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun render(product: ProductPresentationModel, listener: ProductsListener) {
        binding.product = product
        binding.listener = listener
        binding.executePendingBindings()
    }
}