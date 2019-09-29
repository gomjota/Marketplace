package com.juangomez.presentation.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.juangomez.presentation.R
import com.juangomez.presentation.common.toast
import com.juangomez.presentation.common.visible
import com.juangomez.presentation.databinding.CheckoutActivityBinding
import com.juangomez.presentation.models.CheckoutPresentationModel
import com.juangomez.presentation.viewmodels.CheckoutListener
import com.juangomez.presentation.viewmodels.CheckoutViewModel
import com.juangomez.presentation.views.adapters.CheckoutAdapter
import com.juangomez.presentation.views.base.BaseActivity
import kotlinx.android.synthetic.main.checkout_activity.*
import kotlinx.android.synthetic.main.products_activity.cart_group
import kotlinx.android.synthetic.main.products_activity.recycler_view
import org.koin.android.viewmodel.ext.android.viewModel

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
        viewModel.state.observe(this, Observer { onCheckoutStateChange(it) })

        initializeAdapter()
        initializeRecyclerView()
        viewModel.prepare()
    }

    private fun onCheckoutStateChange(state: CheckoutViewModel.CheckoutState?) {
        when (val productsState = state!!) {
            is CheckoutViewModel.CheckoutState.Empty -> {
                finishActivity()
            }
            is CheckoutViewModel.CheckoutState.Cart -> {
                showCheckoutProducts(productsState.products)
                setPayText(productsState.price)
                showCartResume()
            }
            is CheckoutViewModel.CheckoutState.Complete -> {
                showPaymentCompleted()
                finishActivity()
            }
            is CheckoutViewModel.CheckoutState.Error -> {
                showError()
            }
        }
    }

    private fun showCartResume() {
        cart_group.visible()
    }

    private fun setPayText(price: Float) {
        pay_text.text = String.format(getString(R.string.checkout_pay), price)
    }

    override fun configureBinding(binding: CheckoutActivityBinding) {
        binding.listener = viewModel as CheckoutListener
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

    private fun showPaymentCompleted() = runOnUiThread {
        getString(R.string.purchase_completed).toast(this)
    }

    private fun finishActivity() = runOnUiThread {
        this.finish()
    }
}