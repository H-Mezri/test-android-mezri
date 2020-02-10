package com.mezri.bigburger.ui.burgerdetails

import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.data.repository.Repository
import com.mezri.bigburger.ui.base.BaseViewModel
import com.mezri.bigburger.utils.schedulers.BaseSchedulerProvider

class ProductDetailsFragmentViewModel(
    repository: Repository,
    schedulerProvider: BaseSchedulerProvider
) : BaseViewModel(repository, schedulerProvider) {

    // view product
    lateinit var product: Product

    fun initBurger(product: Product) {
        this.product = product
    }
}
