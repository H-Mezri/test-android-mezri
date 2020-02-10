package com.mezri.bigburger.ui.main

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.mezri.bigburger.R
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.ui.MainActivityInterface
import com.mezri.bigburger.ui.base.BaseFragment
import com.mezri.bigburger.ui.basket.BasketFragment
import com.mezri.bigburger.ui.burgerdetails.ProductDetailsFragment
import kotlinx.android.synthetic.main.main_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : BaseFragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    // fragment view model
    private val fragmentViewModel: MainFragmentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementReturnTransition =
                TransitionInflater.from(context).inflateTransition(android.R.transition.move)
            exitTransition =
                TransitionInflater.from(context)
                    .inflateTransition(android.R.transition.no_transition)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
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
        (requireActivity() as AppCompatActivity).setTitle(R.string.app_name)
        // Enable option menu
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        // init action bar
        initActionBar()
        if (fragmentViewModel.productsListCache.size == 0) {
            // load albums if list is empty
            loadBurgersList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.basket -> {
                (requireActivity() as MainActivityInterface).showFragment(
                    BasketFragment.newInstance(),
                    null
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerViewList() {
        // Init recycler view
        recyclerProductsList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)

            adapter = ProductRecyclerAdapter(
                mutableListOf(),
                object :
                    ProductRecyclerAdapter.OnItemClickListener {
                    override fun onAddToBasket(product: Product) {
                        fragmentViewModel.addProductToBasket(product)
                    }

                    override fun onClick(view: View, product: Product) {
                        (requireActivity() as MainActivityInterface).showFragment(
                            ProductDetailsFragment.newInstance(product),
                            view
                        )
                    }
                })
        }
    }

    /**
     * Update UI and start loading burgers list from view model
     */
    private fun loadBurgersList() {
        // if there is no request sent
        fragmentViewModel.getProductsList()
    }

    /**
     * Init View observers
     */
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

        // observe no product loaded
        fragmentViewModel.isNoProductLoaded.observe(
            viewLifecycleOwner,
            Observer { isNoAlbumLoaded ->
                if (isNoAlbumLoaded) {
                    lytNoMusicFound.visibility = View.VISIBLE
                    btnLoadProducts.setOnClickListener { loadBurgersList() }
                } else {
                    lytNoMusicFound.visibility = View.GONE
                }
            })

        // observe app loading albums
        fragmentViewModel.isFirstRequestLoadingProducts.observe(
            viewLifecycleOwner,
            Observer { isFirstRequestLoadingAlbums ->
                if (isFirstRequestLoadingAlbums) {
                    progressBarLoadingProducts.visibility = View.VISIBLE
                } else {
                    progressBarLoadingProducts.visibility = View.GONE
                }
            })

        // observe for view model albums list
        fragmentViewModel.productsList.observe(
            viewLifecycleOwner,
            Observer { newAlbumsList ->
                when {
                    newAlbumsList.isNotEmpty() -> {
                        // in case new product loaded
                        (recyclerProductsList.adapter as ProductRecyclerAdapter).onNewProductsListLoaded(
                            newAlbumsList
                        )
                    }
                    recyclerProductsList.adapter?.itemCount == 0 -> {
                        // in case reload product from cache
                        (recyclerProductsList.adapter as ProductRecyclerAdapter).onNewProductsListLoaded(
                            fragmentViewModel.productsListCache.toMutableList()
                        )
                    }
                    else -> {
                        // in case no more product to load
                        (recyclerProductsList.adapter as ProductRecyclerAdapter).onNewProductsListLoaded(
                            mutableListOf()
                        )
                    }
                }
            })
    }

    /**
     * UnSubscribing from observers
     */
    override fun removeObservers() {
        // clean observers
        if (fragmentViewModel.isNoProductLoaded.hasObservers()) {
            fragmentViewModel.isNoProductLoaded.removeObservers(viewLifecycleOwner)
        }
        if (fragmentViewModel.isFirstRequestLoadingProducts.hasObservers()) {
            fragmentViewModel.isFirstRequestLoadingProducts.removeObservers(viewLifecycleOwner)
        }
        if (fragmentViewModel.productsList.hasObservers()) {
            fragmentViewModel.productsList.removeObservers(viewLifecycleOwner)
        }
        if (fragmentViewModel.informationToShow.hasObservers()) {
            fragmentViewModel.informationToShow.removeObservers(viewLifecycleOwner)
        }
    }

    override fun getViewModel() = fragmentViewModel
}
