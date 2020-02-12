package com.mezri.bigburger.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mezri.bigburger.data.errors.AppMessages
import com.mezri.bigburger.ui.base.BaseViewModelTest
import io.reactivex.Single
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.test.inject
import org.mockito.Mockito.`when`


@RunWith(JUnit4::class)
class MainFragmentViewModelTest : BaseViewModelTest() {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    // View model to test
    private val mainFragmentViewModel: MainFragmentViewModel by inject()

    @Test
    fun testGetProducts_OK() = runBlocking {
        // given
        `when`(mainFragmentViewModel.repository.loadProductList()).thenReturn(
            Single.just(productsList)
        )
        assert(mainFragmentViewModel.productsListCache.isEmpty())

        // when
        mainFragmentViewModel.getProductsList()

        // then
        assert(mainFragmentViewModel.productsListCache.size == 1 && mainFragmentViewModel.productsListCache[0] == productsList[0])
    }

    @Test
    fun testGetProducts_KO_NETWORK_ERROR() = runBlocking {
        // given
        val throwable = Throwable()
        `when`(mainFragmentViewModel.repository.loadProductList()).thenReturn(Single.error(throwable))
        assert(mainFragmentViewModel.productsListCache.isEmpty())

        // when
        mainFragmentViewModel.getProductsList()

        // then
        assert(mainFragmentViewModel.productsListCache.isEmpty())
        assert(
            mainFragmentViewModel.informationToShow.value!! == AppMessages.NETWORK_ERROR.getAppError(
                throwable
            )
        )
    }

    @Test
    fun testGetProducts_KO_ALBUMS_NOT_FOUND() = runBlocking {
        // given
        `when`(mainFragmentViewModel.repository.loadProductList()).thenReturn(
            Single.just(listOf())
        )
        assert(mainFragmentViewModel.productsListCache.isEmpty())

        // when
        mainFragmentViewModel.getProductsList()

        // then
        assert(mainFragmentViewModel.productsListCache.isEmpty())
        assert(mainFragmentViewModel.informationToShow.value!! == AppMessages.PRODUCTS_NOT_FOUND.getAppError())
    }
}