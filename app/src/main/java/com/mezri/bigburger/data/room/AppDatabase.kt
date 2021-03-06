package com.mezri.bigburger.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.data.network.dto.ProductDTO

@Database(entities = [ProductDTO::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val basketProductDAO: BasketProductDAO

    suspend fun addProductToBasket(productDTO: ProductDTO) =
        basketProductDAO.insertProductInBasket(productDTO)

    suspend fun removeProductFromBasket(productDTO: ProductDTO) =
        basketProductDAO.removeProductFromBasket(productDTO)

    suspend fun loadProductsList(): MutableList<ProductDTO> =
        basketProductDAO.selectBasketProducts()

    suspend fun updateProductAmount(product: Product, amount: Byte) =
        basketProductDAO.updateProductAmount(product.id, amount)
}