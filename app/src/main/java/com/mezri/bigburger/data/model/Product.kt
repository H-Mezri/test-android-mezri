package com.mezri.bigburger.data.model

import android.os.Parcel
import android.os.Parcelable
import com.mezri.bigburger.data.network.dto.EMPTY
import com.mezri.bigburger.data.network.dto.ProductDTO
import com.mezri.bigburger.data.network.dto.UNKNOWN_VALUE

const val PRICE_STRING_FORMAT = "%.2f"
const val COMMA = ","
const val DOT = "."

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: String,
    val price: Float,
    var amount: Byte
) :
    Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: UNKNOWN_VALUE,
        parcel.readString() ?: UNKNOWN_VALUE,
        parcel.readString() ?: UNKNOWN_VALUE,
        parcel.readFloat(),
        parcel.readByte()
    )

    constructor(productDTO: ProductDTO) : this(
        productDTO.ref,
        productDTO.title,
        productDTO.description,
        productDTO.thumbnail,
        productDTO.getPriceAsFloat(),
        productDTO.amount
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(thumbnail)
        parcel.writeFloat(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getPriceAsInt(): Int {
        return PRICE_STRING_FORMAT.format(price).replace(COMMA, EMPTY).replace(DOT, EMPTY).toInt()
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}