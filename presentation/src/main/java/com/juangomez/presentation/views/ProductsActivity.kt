package com.juangomez.presentation.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.juangomez.presentation.R
import com.juangomez.presentation.common.gone
import com.juangomez.presentation.common.visible
import com.juangomez.presentation.databinding.ProductsActivityBinding
import com.juangomez.presentation.models.ProductPresentationModel
import com.juangomez.presentation.viewmodels.ProductsListener
import com.juangomez.presentation.viewmodels.ProductsViewModel
import com.juangomez.presentation.views.adapters.ProductsAdapter
import com.juangomez.presentation.views.base.BaseActivity
import kotlinx.android.synthetic.main.products_activity.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProductsActivity : BaseActivity<ProductsActivityBinding>() {

    private val viewModel: ProductsViewModel by viewModel()
    override val layoutId: Int = R.layout.products_activity

    private lateinit var adapter: ProductsAdapter

    companion object {
        fun open(activity: Activity) {
            val intent = Intent(activity, ProductsActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.observe(this, Observer { onProductsStateChange(it) })

        initializeAdapter()
        initializeRecyclerView()
        viewModel.prepare()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCart()
    }

    private fun onProductsStateChange(state: ProductsViewModel.ProductsState?) {
        when (val productsState = state!!) {
            is ProductsViewModel.ProductsState.Loading -> {
                showLoading()
                hideEmptyState()
            }
            is ProductsViewModel.ProductsState.Empty -> {
                hideLoading()
                showEmptyState()
            }
            is ProductsViewModel.ProductsState.Products -> {
                showProducts(productsState.products)
                hideLoading()
                hideEmptyState()
            }
            is ProductsViewModel.ProductsState.Cart -> {
                setCartText(productsState.productsInCart)
                manageCartResume(productsState.productsInCart)
            }
            is ProductsViewModel.ProductsState.Checkout -> {
                openCheckout()
            }
            is ProductsViewModel.ProductsState.Error -> {
                hideLoading()
                hideEmptyState()
                showError()
            }
        }
    }

    private fun showLoading() {
        progress_bar.show()
    }

    private fun hideLoading() {
        progress_bar.hide()
    }

    private fun showEmptyState() {
        empty_case.visible()
    }

    private fun hideEmptyState() {
        empty_case.gone()
    }

    private fun manageCartResume(productsInCart: Int) {
        if (productsInCart > 0) cart_group.visible() else cart_group.gone()
    }

    private fun setCartText(amountOfProductsInCart: Int) {
        cart_text.text = String.format(getString(R.string.cart_purchase), amountOfProductsInCart)
    }

    override fun configureBinding(binding: ProductsActivityBinding) {
        binding.listener = viewModel as ProductsListener
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

    private fun openCheckout() = runOnUiThread {
        CheckoutActivity.open(this)
    }
}