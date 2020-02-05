package com.mezri.bigburger.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.data.network.dto.ProductDTO

private const val DATABASE_NAME = "bigburger.room.db"

@Database(entities = [ProductDTO::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val basketProductDAO: BasketProductDAO

    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return instance!!
        }
    }

    fun addProductToBasket(productDTO: ProductDTO) =
        basketProductDAO.insertProductInBasket(productDTO)

    fun removeProductFromBasket(productDTO: ProductDTO) =
        basketProductDAO.removeProductFromBasket(productDTO)

    fun loadProductsList(): MutableList<ProductDTO> = basketProductDAO.selectBasketProducts()

    fun updateProductAmount(product: Product, amount: Byte) =
        basketProductDAO.updateProductAmount(product.id, amount)
}