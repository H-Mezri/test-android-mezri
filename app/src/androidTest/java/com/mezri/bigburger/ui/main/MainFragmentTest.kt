package com.mezri.bigburger.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.mezri.bigburger.R
import com.mezri.bigburger.ui.MainActivity
import com.mezri.bigburger.ui.utils.RecyclerViewItemAssertion
import com.mezri.bigburger.utils.idling.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

    @get:Rule
    private val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    fun setup() {
        activityRule.launchActivity(null)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testRecyclerView_OK_show_products_list() {
        // first product to receive in the list
        val firstProductTitle = "The Big Burger"

        // Check if product title exist in the recycler view after data is displayed
        onView(withId(R.id.recyclerProductsList)).check(
            RecyclerViewItemAssertion(
                firstProductTitle,
                true
            )
        )
    }

    @Test
    fun testOnChooseProduct_ViewProductDetailsIsVisible() {
        // perform click on recycler view item
        onView(withId(R.id.recyclerProductsList)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductRecyclerAdapter.ProductItemViewHolder>(
                0,
                click()
            )
        )
        // after performing a click if buttons from second fragment are displayed on the view
        onView(withId(R.id.btnAddToBasket)).check(matches(isDisplayed()))
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }
}