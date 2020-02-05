package com.mezri.bigburger.data.network.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

const val DOT = "."
const val EMPTY = ""

@Entity(tableName = "PRODUCT")
data class ProductDTO(
    @PrimaryKey val ref: Int,
    val title: String,
    val description: String,
    val thumbnail: String,
    val price: Int,
    var amount: Byte
) {
    /**
     * Convert price from int to float
     * Remote server send prices in euro cents (820) then we must convert the price to euros (8.20)
     */
    fun getPriceAsFloat(): Float {
        return StringBuilder().run {
            append(price)
            while (this.toString().length < 3) {
                insert(0, 0)
            }
            insert(this.toString().length - 2, DOT)
            toString()
        }.toFloat()
    }
}