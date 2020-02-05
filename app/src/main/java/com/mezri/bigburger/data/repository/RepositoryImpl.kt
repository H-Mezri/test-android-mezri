package com.mezri.bigburger.data.repository

import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.data.network.ProductsAPI
import com.mezri.bigburger.data.network.RetrofitClient
import com.mezri.bigburger.data.network.dto.DTOMapper
import com.mezri.bigburger.data.network.dto.ProductDTO
import com.mezri.bigburger.data.room.AppDatabase
import io.reactivex.Single

class RepositoryImpl(private val appDatabase: AppDatabase) : Repository {

    override fun loadBasketProductList(): MutableList<Product> {
        return mapProduct(appDatabase.loadProductsList()).toMutableList()
    }

    override fun addProductToBasket(product: Product): Long {
        return appDatabase.addProductToBasket(mapToBasketProductDTO(product))
    }

    override fun removeProductFromBasket(product: Product): Int {
        return appDatabase.removeProductFromBasket(mapToBasketProductDTO(product))
    }

    override fun updateProductAmount(product: Product, amount: Byte): Int {
        return appDatabase.updateProductAmount(product, amount)
    }

    private val networkService by lazy {
        RetrofitClient.getNetworkClient().create(ProductsAPI::class.java)
    }

    /**
     * Get products from remote server
     * /!\ /!\ In a bigger project repository must check if the data already exist in a local database an return it
     * if the data not found in local database or data is old, then trigger network request then save the result in local database
     */
    override fun loadProductList(): Single<List<Product>> {
        return networkService.loadProducts().map { mapProduct(it) }
    }

    private fun mapProduct(networkProductsList: List<ProductDTO>): List<Product> {
        val productDataMapper: DTOMapper<ProductDTO, Product> =
            object : DTOMapper<ProductDTO, Product> {
                override fun map(input: ProductDTO): Product {
                    return Product(input)
                }
            }

        return networkProductsList.map { productDataMapper.map(it) }
    }

    private fun mapToBasketProductDTO(product: Product): ProductDTO {
        return ProductDTO(
            product.id,
            product.title,
            product.description,
            product.thumbnail,
            product.getPriceAsInt(),
            product.amount
        )
    }
}