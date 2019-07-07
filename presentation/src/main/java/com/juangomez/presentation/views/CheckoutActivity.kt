package com.juangomez.presentation.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.juangomez.presentation.R
import com.juangomez.presentation.databinding.CheckoutActivityBinding
import com.juangomez.presentation.models.CheckoutPresentationModel
import com.juangomez.presentation.viewmodels.CheckoutViewModel
import com.juangomez.presentation.views.adapters.CheckoutAdapter
import com.juangomez.presentation.views.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.products_activity.*

class CheckoutActivity : BaseActivity<CheckoutActivityBinding>() {

    private val viewModel: CheckoutViewModel by viewModel()
    override val layoutId: Int = R.layout.checkout_activity

    private lateinit var adapter: CheckoutAdapter

    companion object {
        fun open(activity: Activity) {
            val intent = Intent(activity, CheckoutActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeAdapter()
        initializeRecyclerView()
        viewModel.checkoutProductsToShow.observe(this, Observer { showCheckoutProducts(it) })
        viewModel.prepare()
    }

    override fun configureBinding(binding: CheckoutActivityBinding) {
        binding.viewModel = viewModel
    }

    private fun initializeAdapter() {
        adapter = CheckoutAdapter(viewModel)
    }

    private fun initializeRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter
    }

    private fun showCheckoutProducts(checkout: List<CheckoutPresentationModel>) = runOnUiThread {
        adapter.clear()
        adapter.addAll(checkout)
        adapter.notifyDataSetChanged()
    }
}