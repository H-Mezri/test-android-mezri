package com.mezri.bigburger.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mezri.bigburger.data.network.dto.ProductDTO

@Dao
interface BasketProductDAO {

    @Insert
    fun insertProductInBasket(productDTO: ProductDTO): Long

    @Query("SELECT * FROM PRODUCT")
    fun selectBasketProducts(): MutableList<ProductDTO>

    @Delete
    fun removeProductFromBasket(productDTO: ProductDTO): Int

    @Query("UPDATE PRODUCT SET amount = amount + :amount WHERE ref = :productId")
    fun updateProductAmount(productId: Int, amount: Byte): Int
}