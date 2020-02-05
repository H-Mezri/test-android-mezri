package com.mezri.bigburger.data.repository

import com.mezri.bigburger.data.model.Product
import io.reactivex.Single

interface Repository {
    /**
     * load product list from remote server
     */
    fun loadProductList(): Single<List<Product>>

    /**
     * Add product to basket
     */
    fun addProductToBasket(product: Product): Long

    /**
     * Remove product from basket
     */
    fun removeProductFromBasket(product: Product): Int

    /**
     * Update product amount in basket
     */
    fun updateProductAmount(product: Product, amount: Byte): Int

    /**
     * Load products from basket
     */
    fun loadBasketProductList(): MutableList<Product>
}
