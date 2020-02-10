package com.mezri.bigburger.utils.koin

import androidx.room.Room
import com.mezri.bigburger.data.repository.Repository
import com.mezri.bigburger.data.repository.RepositoryImpl
import com.mezri.bigburger.data.room.AppDatabase
import com.mezri.bigburger.ui.basket.BasketFragmentViewModel
import com.mezri.bigburger.ui.burgerdetails.ProductDetailsFragmentViewModel
import com.mezri.bigburger.ui.main.MainFragmentViewModel
import com.mezri.bigburger.utils.DATABASE_NAME
import com.mezri.bigburger.utils.schedulers.BaseSchedulerProvider
import com.mezri.bigburger.utils.schedulers.SchedulerProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Single instance of app database
    single { Room.databaseBuilder(get(), AppDatabase::class.java, DATABASE_NAME).allowMainThreadQueries().build() }
    // Single instance of the repository
    single { RepositoryImpl(get()) as Repository }
    // Factory instance of the scheduler provider
    factory { SchedulerProvider() as BaseSchedulerProvider }
    // View model providers
    viewModel { MainFragmentViewModel(get(), get()) }
    viewModel { ProductDetailsFragmentViewModel(get(), get()) }
    viewModel { BasketFragmentViewModel(get(), get()) }
}