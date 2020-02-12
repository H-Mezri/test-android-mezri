package com.mezri.bigburger.ui.base

import com.mezri.bigburger.data.model.Product
import com.mezri.bigburger.data.repository.Repository
import com.mezri.bigburger.ui.main.MainFragmentViewModel
import com.mezri.bigburger.utils.schedulers.BaseSchedulerProvider
import com.mezri.bigburger.utils.schedulers.TrampolineSchedulerProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.Mockito

abstract class BaseViewModelTest : KoinTest {

    private val testModule = module {
        // Single instance of the repository
        single { Mockito.mock(Repository::class.java) }
        // Factory instance of the scheduler provider
        factory { TrampolineSchedulerProvider() as BaseSchedulerProvider }
        // View model providers
        viewModel { MainFragmentViewModel(get(), get()) }
    }

    val productsList = listOf(
        Product(1, "title", "description", "http://thumbnail", 12.39f, 2)
    )

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    open fun before() {
        // start Koin
        startKoin { modules(testModule) }
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    open fun after() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
        stopKoin()
    }
}