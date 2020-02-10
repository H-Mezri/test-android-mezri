package com.mezri.bigburger

import android.app.Application
import com.mezri.bigburger.data.network.HttpClient
import com.mezri.bigburger.utils.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // init Koin
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
        // init cache directory for http client
        HttpClient.initCacheDirectory(cacheDir)
    }
}