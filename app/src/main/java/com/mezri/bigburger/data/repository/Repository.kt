package com.mezri.bigburger.data.repository

import com.mezri.bigburger.data.model.Product
import io.reactivex.Single

interface Repository {
    /**
     * load product list from remote server
     */
    suspend fun loadProductList(): Single<List<Product>>

    /**
     * Add product to basket
     */
    suspend fun addProductToBasket(product: Product): Long

    /**
     * Remove product from basket
     */
    suspend fun removeProductFromBasket(product: Product): Int

    /**
     * Update product amount in basket
     */
    suspend fun updateProductAmount(product: Product, amount: Byte): Int

    /**
     * Load products from basket
     */
    suspend fun loadBasketProductList(): MutableList<Product>
}
