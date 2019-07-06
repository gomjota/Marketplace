package com.juangomez.presentation.views

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.juangomez.presentation.R
import com.juangomez.presentation.databinding.ProductsActivityBinding
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.viewmodels.ProductsViewModel
import com.juangomez.presentation.views.adapters.ProductsAdapter
import com.juangomez.presentation.views.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.products_activity.*

class ProductsActivity : BaseActivity<ProductsActivityBinding>() {

    private val viewModel: ProductsViewModel by viewModel()
    override val layoutId: Int = R.layout.products_activity

    private lateinit var adapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeAdapter()
        initializeRecyclerView()
        viewModel.products.observe(this, Observer { showProducts(it) })
        viewModel.prepare()
    }

    override fun configureBinding(binding: ProductsActivityBinding) {
        binding.viewModel = viewModel
    }

    private fun initializeAdapter() {
        adapter = ProductsAdapter(viewModel)
    }

    private fun initializeRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter
    }

    private fun showProducts(products: List<ProductPresentationModel>) = runOnUiThread {
        adapter.clear()
        adapter.addAll(products)
        adapter.notifyDataSetChanged()
    }
}