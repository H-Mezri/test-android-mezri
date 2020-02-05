package com.mezri.bigburger.utils.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.mezri.bigburger.R

fun ImageView.load(
    url: String,
    loadOnlyFromCache: Boolean = false,
    animateImage: Boolean = false,
    onLoadingFinished: () -> Unit
) {

    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }
    }

    var requestOptions =
        RequestOptions
            .placeholderOf(R.drawable.ic_food_cover)
            .error(R.drawable.ic_broken_image)
            .dontTransform()
            .onlyRetrieveFromCache(loadOnlyFromCache)

    if (!animateImage) {
        requestOptions = requestOptions.dontAnimate()
    }

    GlideApp.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .apply(requestOptions)
        .listener(listener)
        .into(this)
}