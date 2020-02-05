package com.mezri.bigburger.ui.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.ui.main.ProductRecyclerAdapter
import org.hamcrest.Matchers.*

class RecyclerViewItemAssertion(private val title: String, private val contains: Boolean) :
    ViewAssertion {

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter as ProductRecyclerAdapter
        val adapterList = adapter.productList

        if (contains) {
            assertThat((adapterList[0] as Product).title, containsString(title))
        } else {
            assertThat(
                (adapterList[0] as Product).title,
                `is`(not(containsString(title)))
            )
        }
    }
}