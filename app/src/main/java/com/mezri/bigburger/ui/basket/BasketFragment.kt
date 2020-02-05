package com.mezri.bigburger.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mezri.bigburger.R
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.ui.base.BaseFragment
import com.mezri.bigburger.ui.base.BaseViewModel
import kotlinx.android.synthetic.main.basket_fragment.*

class BasketFragment : BaseFragment() {

    companion object {
        fun newInstance() = BasketFragment()
    }

    // fragment view model
    private lateinit var fragmentViewModel: BasketFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // init view model
        fragmentViewModel = ViewModelProviders.of(this).get(BasketFragmentViewModel::class.java)

        // inject dependencies in view model
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.basket_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init recycler view list
        initRecyclerViewList()
    }

    /**
     * Init main toolbar
     */
    private fun initActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(mainToolbar)
        (requireActivity() as AppCompatActivity).setTitle(R.string.basket)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        // Enable option menu
        setHasOptionsMenu(true)
    }

    /**
     * Handle option menu click
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerViewList() {
        // Init recycler view
        recyclerBasketProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())

            adapter = BasketRecyclerAdapter(
                mutableListOf(),
                object :
                    BasketRecyclerAdapter.OnItemClickListener {
                    override fun onAddToBasket(product: Product) {
                        fragmentViewModel.addProductToBasket(product)
                    }

                    override fun onRemoveFromBasket(product: Product) {
                        fragmentViewModel.removeProductFromBasket(product)
                    }
                })
        }
    }

    override fun removeObservers() {
        if (fragmentViewModel.informationToShow.hasObservers()) {
            fragmentViewModel.informationToShow.removeObservers(viewLifecycleOwner)
        }
        if (fragmentViewModel.isBasketProductsChanged.hasObservers()) {
            fragmentViewModel.isBasketProductsChanged.removeObservers(viewLifecycleOwner)
        }
    }

    override fun initObservers() {
        // observer for app message and errors
        fragmentViewModel.informationToShow.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Snackbar.make(
                    rootLayout,
                    getString(it.messageId),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        fragmentViewModel.isBasketProductsChanged.observe(viewLifecycleOwner, Observer {
            (recyclerBasketProducts.adapter as BasketRecyclerAdapter).updateBasketProducts(
                fragmentViewModel.basketProducts
            )
            // update total basket price
            txtBasketPrice.text =
                getString(R.string.basket_price, fragmentViewModel.basketTotalPrice)
        })
    }

    override fun onResume() {
        super.onResume()
        initActionBar()
        // calculate new basket price
        fragmentViewModel.calculateBasketTotalPrice()
        // update total basket price
        txtBasketPrice.text = getString(R.string.basket_price, fragmentViewModel.basketTotalPrice)
    }

    override fun getFragmentViewModel(): BaseViewModel = fragmentViewModel
}