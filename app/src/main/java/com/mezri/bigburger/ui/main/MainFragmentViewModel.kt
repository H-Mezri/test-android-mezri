package com.mezri.bigburger.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mezri.bigburger.data.errors.AppMessages
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.data.repository.Repository
import com.mezri.bigburger.ui.base.BaseViewModel
import com.mezri.bigburger.utils.idling.EspressoIdlingResource
import com.mezri.bigburger.utils.schedulers.BaseSchedulerProvider
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainFragmentViewModel(repository: Repository, schedulerProvider: BaseSchedulerProvider) :
    BaseViewModel(repository, schedulerProvider) {

    // list of product in cache
    val productsListCache = mutableListOf<Product>()

    // indicate if there is already a loading product request
    var isLoadingProducts: Boolean = false

    // live data for fragment
    val productsList = MutableLiveData<List<Any>>().apply { value = mutableListOf() }
    var isFirstRequestLoadingProducts = MutableLiveData<Boolean>().apply { value = true }
    var isNoProductLoaded = MutableLiveData<Boolean>().apply { value = false }

    /**
     * Send products request and handle the response
     */
    fun getProductsList() {
        // Inform Espresso that app is busy
        EspressoIdlingResource.increment()
        // notify app is loading data
        isLoadingProducts = true
        // check is has previous error
        if (isNoProductLoaded.value!!) {
            isNoProductLoaded.value = false
            isFirstRequestLoadingProducts.value = true
        }

        // call network service to load albums
        viewModelScope.launch {
            repository.loadProductList()
                .observeOn(schedulerProvider.ui())
                .subscribeOn(schedulerProvider.io())
                .subscribe({ products ->
                    when {
                        products.isNotEmpty() -> {
                            // add request result to albums list
                            productsList.value = products
                            productsListCache.addAll(products)
                            isFirstRequestLoadingProducts.value = false
                        }
                        productsListCache.size == 0 -> {
                            // in case no albums found
                            productsList.value = emptyList()
                            // handle error
                            handleAppMessage(AppMessages.PRODUCTS_NOT_FOUND.getAppError())
                        }
                        else -> {
                            // in case no more albums to load
                            productsList.value = emptyList()
                        }
                    }
                    // notify app finished loading data
                    isLoadingProducts = false
                    // Inform Espresso that app is ready
                    EspressoIdlingResource.decrement()
                }, { throwable ->
                    if (productsListCache.isEmpty()) {
                        // notify request received
                        isNoProductLoaded.value = true
                    }
                    isFirstRequestLoadingProducts.value = false
                    // notify app finished loading data
                    productsList.value = emptyList()
                    isLoadingProducts = false
                    // handle error
                    handleAppMessage(AppMessages.NETWORK_ERROR.getAppError(throwable))
                    // Inform Espresso that app is ready
                    EspressoIdlingResource.decrement()
                })
        }
    }

    override fun clearTemporaryData() {
        super.clearTemporaryData()
        productsList.value = mutableListOf()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
