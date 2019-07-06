package com.juangomez.presentation.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.juangomez.presentation.R
import com.juangomez.presentation.databinding.ProductRowBinding
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.viewmodels.ProductsViewModel

internal class ProductsAdapter(
    private val viewModel: ProductsViewModel
) : RecyclerView.Adapter<ProductsViewHolder>() {
    private val products: MutableList<ProductPresentationModel> = ArrayList()

    fun addAll(collection: Collection<ProductPresentationModel>) {
        products.addAll(collection)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding: ProductRowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.product_row,
            parent,
            false
        )

        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = products[position]
        holder.render(product, viewModel)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun clear() {
        products.clear()
    }
}