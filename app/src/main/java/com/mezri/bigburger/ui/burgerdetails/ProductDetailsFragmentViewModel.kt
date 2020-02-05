package com.mezri.bigburger.ui.burgerdetails

import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.ui.base.BaseViewModel

class ProductDetailsFragmentViewModel : BaseViewModel() {

    // view product
    lateinit var product: Product

    fun initBurger(product: Product) {
        this.product = product
    }
}
