package com.mezri.bigburger.ui.main

import android.os.Build
import com.mezri.bigburger.data.errors.AppMessages
import com.mezri.bigburger.data.repository.Repository
import com.mezri.bigburger.ui.base.BaseViewModelTest
import com.mezri.bigburger.utils.schedulers.TrampolineSchedulerProvider
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])
class MainFragmentViewModelTest : BaseViewModelTest() {

    // View model to test
    private lateinit var mainFragmentViewModel: MainFragmentViewModel

    @Mock
    lateinit var repository: Repository

    override fun setup() {
        super.setup()
        // init view model
        mainFragmentViewModel = MainFragmentViewModel()
        mainFragmentViewModel.initViewModelDependencies(repository, TrampolineSchedulerProvider())
    }

    @Test
    fun testGetProducts_OK() {
        // given
        `when`(repository.loadProductList()).thenReturn(
            Single.just(productsList)
        )
        assert(mainFragmentViewModel.productsListCache.isEmpty())

        // when
        mainFragmentViewModel.getProductsList()

        // then
        assert(mainFragmentViewModel.productsListCache.size == 1 && mainFragmentViewModel.productsListCache[0] == productsList[0])
    }

    @Test
    fun testGetProducts_KO_NETWORK_ERROR() {
        // given
        val throwable = Throwable()
        `when`(repository.loadProductList()).thenReturn(Single.error(throwable))
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
    fun testGetProducts_KO_ALBUMS_NOT_FOUND() {
        // given
        `when`(repository.loadProductList()).thenReturn(
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