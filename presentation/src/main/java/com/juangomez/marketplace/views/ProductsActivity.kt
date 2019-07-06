package com.juangomez.marketplace.views

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.juangomez.marketplace.R
import com.juangomez.marketplace.databinding.ProductsActivityBinding
import com.juangomez.marketplace.models.ProductPresentationModel
import com.juangomez.marketplace.viewmodels.ProductsViewModel
import com.juangomez.marketplace.views.adapters.ProductsAdapter
import com.juangomez.marketplace.views.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.products_activity.*

class ProductsActivity : BaseActivity<ProductsActivityBinding>() {

    private val viewModel: ProductsViewModel by viewModel()
    override val layoutId: Int = R.layout.products_activity
    override val toolbarView: Toolbar
        get() = toolbar

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