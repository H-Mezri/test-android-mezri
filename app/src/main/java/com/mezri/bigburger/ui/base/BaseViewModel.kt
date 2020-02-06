package com.mezri.bigburger.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mezri.bigburger.data.errors.AppInformation
import com.mezri.bigburger.data.errors.AppMessages
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.data.repository.Repository
import com.mezri.bigburger.utils.schedulers.BaseSchedulerProvider

abstract class BaseViewModel : ViewModel() {

    protected val TAG = this::class.java.simpleName

    // Schedulers
    protected lateinit var schedulerProvider: BaseSchedulerProvider
    // view model repository
    protected lateinit var repository: Repository
    // products in basket list
    val basketProducts: MutableList<Product> = mutableListOf()

    /**
     * inject dependencies into view model (repository, schedulers)
     * {in a bigger project we can use Dagger2 to inject repository in view models}
     */
    fun initViewModelDependencies(
        repository: Repository,
        schedulerProvider: BaseSchedulerProvider
    ) {
        this.repository = repository
        this.schedulerProvider = schedulerProvider
    }

    /**
     * Add product to basket
     */
    open fun addProductToBasket(product: Product) {
        var basketProduct = basketProducts.find { it.id == product.id }
        if (basketProduct != null) {
            repository.updateProductAmount(basketProduct, 1)
            basketProduct.amount++
        } else {
            basketProduct = product.copy()
            basketProduct.amount++
            repository.addProductToBasket(basketProduct)
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
                repository.updateProductAmount(product, -1)
                basketProduct.amount--
            }
            else -> {
                repository.removeProductFromBasket(basketProduct)
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
    }

    fun loadBasketProducts() {
        basketProducts.addAll(repository.loadBasketProductList())
    }
}