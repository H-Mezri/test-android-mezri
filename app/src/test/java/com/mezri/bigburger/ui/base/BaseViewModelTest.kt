package com.mezri.bigburger.ui.base

import com.mezri.bigburger.data.model.Product
import org.junit.Before
import org.mockito.MockitoAnnotations

abstract class BaseViewModelTest {

    val productsList = listOf(
        Product(1, "title", "description", "http://thumbnail", 12.39f, 2)
    )

    @Before
    open fun setup() {
        // init mock objects
        MockitoAnnotations.initMocks(this)
    }
}