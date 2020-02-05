package com.mezri.bigburger.ui.basket

import androidx.lifecycle.MutableLiveData
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.ui.base.BaseViewModel

class BasketFragmentViewModel : BaseViewModel() {

    val isBasketProductsChanged = MutableLiveData<Boolean>().apply { value = false }
    var basketTotalPrice: Float = 0f

    override fun addProductToBasket(product: Product) {
        super.addProductToBasket(product)
        // calculate new basket price
        basketTotalPrice += product.price
        isBasketProductsChanged.value = true
    }

    override fun removeProductFromBasket(product: Product) {
        super.removeProductFromBasket(product)
        // calculate new basket price
        basketTotalPrice -= product.price
        isBasketProductsChanged.value = true
    }

    fun calculateBasketTotalPrice() {
        for (product in basketProducts) {
            basketTotalPrice += product.price * product.amount
        }
    }

    override fun clearTemporaryData() {
        super.clearTemporaryData()
        basketTotalPrice = 0f
    }
}