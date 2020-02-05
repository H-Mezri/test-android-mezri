package com.mezri.bigburger.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.databinding.ProductItemBinding
import com.mezri.bigburger.utils.glide.load
import kotlinx.android.synthetic.main.product_item.view.*

private const val VIEW_TYPE_ITEM = 0

class ProductRecyclerAdapter(
    val productList: MutableList<Any>,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onClick(view: View, product: Product)
        fun onAddToBasket(product: Product)
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val albumItemBinding = ProductItemBinding.inflate(layoutInflater, parent, false)
        return ProductItemViewHolder(albumItemBinding)
    }

    override fun onBindViewHolder(holderItem: RecyclerView.ViewHolder, position: Int) {
        (holderItem as ProductItemViewHolder).bind(productList[position] as Product)
    }

    inner class ProductItemViewHolder(private val burgerItemBinding: ProductItemBinding) :
        RecyclerView.ViewHolder(burgerItemBinding.root) {
        fun bind(product: Product) {
            burgerItemBinding.product = product

            // load product cover
            itemView.imgBurgerCover.load(product.thumbnail) {}

            // init image view transition name
            ViewCompat.setTransitionName(itemView.imgBurgerCover, product.id.toString())

            // set view on click listener
            itemView.setOnClickListener {
                itemClickListener.onClick(itemView.imgBurgerCover, product)
            }

            // set click listener on add to basket button
            itemView.addToBasket.setOnClickListener {
                itemClickListener.onAddToBasket(product)
            }
        }
    }

    override fun getItemCount(): Int = productList.size

    /**
     * Update list when request result received
     */
    fun onNewProductsListLoaded(newProductsList: List<Any>) {
        if (newProductsList.isNotEmpty()) {
            productList.addAll(newProductsList)
        }
        notifyDataSetChanged()
    }
}