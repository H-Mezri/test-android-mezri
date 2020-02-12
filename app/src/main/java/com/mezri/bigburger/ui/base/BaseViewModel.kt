package com.mezri.bigburger.ui.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mezri.bigburger.data.errors.AppInformation
import com.mezri.bigburger.data.errors.AppMessages
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.data.repository.Repository
import com.mezri.bigburger.utils.schedulers.BaseSchedulerProvider
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    val repository: Repository,
    val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    protected val TAG = this::class.java.simpleName

    // products in basket list
    val basketProducts: MutableList<Product> = mutableListOf()
    var isBasketProductChanged = MutableLiveData<Boolean>().apply { value = false }


    /**
     * Add product to basket
     */
    open fun addProductToBasket(product: Product) {
        var basketProduct = basketProducts.find { it.id == product.id }
        if (basketProduct != null) {
            viewModelScope.launch {
                repository.updateProductAmount(basketProduct!!, 1)
            }
            basketProduct.amount++
        } else {
            basketProduct = product.copy()
            basketProduct.amount++
            viewModelScope.launch {
                repository.addProductToBasket(basketProduct)
            }
            basketProducts.add(basketProduct)
        }
        handleAppMessage(AppMessages.PRODUCT_ADDED_TO_BASKET.getAppError())
    }

    /**
     * Remove product from basket
     */
    open fun removeProductFromBasket(product: Product) {
        val basketProduct = basketProducts.find { it.id == product.id }
        when {
            basketProduct == null -> {
                return
            }
            basketProduct.amount > 1 -> {
                viewModelScope.launch {
                    repository.updateProductAmount(product, -1)
                }
                basketProduct.amount--
            }
            else -> {
                viewModelScope.launch {
                    repository.removeProductFromBasket(basketProduct)
                }
                basketProducts.remove(basketProduct)
            }
        }
        handleAppMessage(AppMessages.PRODUCT_REMOVED_TO_BASKET.getAppError())
    }

    private val _informationToShow = MutableLiveData<AppInformation>().apply { value = null }
    // live data for UI to show app messages
    val informationToShow: LiveData<AppInformation> = _informationToShow

    /**
     * Notify the UI that a new app message is triggered
     */
    fun handleAppMessage(appInformation: AppInformation) {
        _informationToShow.value = appInformation
    }

    open fun clearTemporaryData() {
        _informationToShow.value = null
        basketProducts.clear()
        isBasketProductChanged.value = false
    }

    fun loadBasketProducts() {
        viewModelScope.launch {
            basketProducts.addAll(repository.loadBasketProductList())
            isBasketProductChanged.value = true
        }
    }
}