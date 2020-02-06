package com.mezri.bigburger.ui.basket

import androidx.lifecycle.MutableLiveData
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.ui.base.BaseViewModel

class BasketFragmentViewModel : BaseViewModel() {

    val productUpdated = MutableLiveData<Product>().apply { value = null }
    var basketTotalPrice: Float = 0f

    override fun addProductToBasket(product: Product) {
        super.addProductToBasket(product)
        // calculate new basket price
        basketTotalPrice += product.price
        val basketProductChanged = basketProducts.find { it.id == product.id }?.copy()
        productUpdated.value = basketProductChanged
    }

    override fun removeProductFromBasket(product: Product) {
        super.removeProductFromBasket(product)
        // calculate new basket price
        basketTotalPrice -= product.price
        var basketProductChanged = basketProducts.find { it.id == product.id }?.copy()
        if (basketProductChanged == null) {
            // in case product removed
            basketProductChanged = product.apply { amount = 0 }
        }
        productUpdated.value = basketProductChanged
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