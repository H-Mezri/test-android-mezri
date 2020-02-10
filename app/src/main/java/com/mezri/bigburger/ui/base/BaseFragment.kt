package com.mezri.bigburger.ui.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    override fun onResume() {
        super.onResume()
        // init observers
        initObservers()
        // load basket product list
        getViewModel().loadBasketProducts()

    }

    override fun onPause() {
        super.onPause()
        removeObservers()
        getViewModel().clearTemporaryData()
    }

    abstract fun initObservers()
    abstract fun removeObservers()
    abstract fun getViewModel(): BaseViewModel
}