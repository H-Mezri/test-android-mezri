package com.mezri.bigburger.ui.burgerdetails

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.mezri.bigburger.R
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.databinding.ProductDetailsFragmentBinding
import com.mezri.bigburger.ui.base.BaseFragment
import com.mezri.bigburger.utils.glide.load
import kotlinx.android.synthetic.main.product_details_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsFragment : BaseFragment(), View.OnClickListener {

    // fragment view model
    private val fragmentViewModel: ProductDetailsFragmentViewModel by viewModel()

    companion object {
        private const val PRODUCT_KEY = "product"
        fun newInstance(product: Product): ProductDetailsFragment {
            val fragment = ProductDetailsFragment()

            val bundle = Bundle()
            bundle.putParcelable(PRODUCT_KEY, product)
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // shared elements configuration
        postponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition =
                TransitionInflater.from(context).inflateTransition(android.R.transition.move)
            enterTransition =
                TransitionInflater.from(context)
                    .inflateTransition(android.R.transition.no_transition)
        }

        fragmentViewModel.initBurger(
            arguments?.getParcelable(PRODUCT_KEY) ?: throw Exception("Product not found")
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = DataBindingUtil.inflate<ProductDetailsFragmentBinding>(
            inflater,
            R.layout.product_details_fragment, container, false
        )
        fragmentBinding.viewModel = fragmentViewModel
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // shared elements configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgBurgerCover.transitionName = fragmentViewModel.product.id.toString()
        }

        // load product cover
        imgBurgerCover.load(
            fragmentViewModel.product.thumbnail,
            true
        ) { startPostponedEnterTransition() }

        // init UI buttons
        initUIButtons()
        // init action bar
        initActionBar()
    }

    /**
     * Init UI buttons
     */
    private fun initUIButtons() {
        btnShare.setOnClickListener(this)
        btnFavorites.setOnClickListener(this)
        btnAddToBasket.setOnClickListener(this)
    }

    /**
     * Init collapse app bar
     */
    private fun initActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).title = ""
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        // Enable option menu
        setHasOptionsMenu(true)
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
    }

    override fun removeObservers() {
        if (fragmentViewModel.informationToShow.hasObservers()) {
            fragmentViewModel.informationToShow.removeObservers(viewLifecycleOwner)
        }
    }

    override fun getViewModel() = fragmentViewModel

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

    /**
     * Handle UI buttons click
     */
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnAddToBasket -> {
                fragmentViewModel.addProductToBasket(fragmentViewModel.product)
            }
            else -> {
                Toast.makeText(requireContext(), R.string.todo, Toast.LENGTH_SHORT).show()
            }
        }
    }
}