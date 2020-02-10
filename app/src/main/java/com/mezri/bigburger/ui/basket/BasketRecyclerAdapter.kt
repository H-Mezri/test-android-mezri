package com.mezri.bigburger.ui.basket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.databinding.ProductBasketItemBinding
import com.mezri.bigburger.utils.glide.load
import kotlinx.android.synthetic.main.product_basket_item.view.*

private const val VIEW_TYPE_ITEM = 0

class BasketRecyclerAdapter(
    private val productBasketList: MutableList<Product>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onRemoveFromBasket(product: Product)
        fun onAddToBasket(product: Product)
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val productItemBinding = ProductBasketItemBinding.inflate(layoutInflater, parent, false)
        return ProductBasketItemViewHolder(productItemBinding)
    }

    override fun onBindViewHolder(holderItem: RecyclerView.ViewHolder, position: Int) {
        (holderItem as ProductBasketItemViewHolder).bind(productBasketList[position])
    }

    inner class ProductBasketItemViewHolder(private val productBasketItemBinding: ProductBasketItemBinding) :
        RecyclerView.ViewHolder(productBasketItemBinding.root) {
        fun bind(basketProduct: Product) {
            productBasketItemBinding.product = basketProduct

            // load product cover
            itemView.imgBurgerCover.load(basketProduct.thumbnail) {}

            // set click listener on add to basket button
            itemView.addToBasket.setOnClickListener {
                itemClickListener.onAddToBasket(basketProduct)
            }
            // set click listener on remove from basket button
            itemView.removeFromBasket.setOnClickListener {
                itemClickListener.onRemoveFromBasket(basketProduct)
            }
        }
    }

    override fun getItemCount(): Int = productBasketList.size

    /**
     * On basket product changed notify adapter
     */
    fun updateBasketProducts(productUpdated: Product) {
        val itemChangedPosition = productBasketList.indexOfFirst { it.id == productUpdated.id }
        if (productUpdated.amount.toInt() == 0) {
            productBasketList.removeAt(itemChangedPosition)
            notifyItemRemoved(itemChangedPosition)
        } else {
            productBasketList[itemChangedPosition] = productUpdated
            notifyItemChanged(itemChangedPosition)
        }
    }

    /**
     * On product basket list loaded
     */
    fun setProducts(productBasketList: MutableList<Product>) {
        this.productBasketList.addAll(productBasketList)
        notifyDataSetChanged()
    }
}