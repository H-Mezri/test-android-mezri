package com.mezri.bigburger.data.network

import com.mezri.bigburger.data.network.dto.ProductDTO
import io.reactivex.Single
import retrofit2.http.GET

interface ProductsAPI {
    /**
     * Get request for products
     */
    @GET("catalog")
    fun loadProducts(): Single<List<ProductDTO>>
}