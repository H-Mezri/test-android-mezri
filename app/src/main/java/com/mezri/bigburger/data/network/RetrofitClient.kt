package com.mezri.bigburger.data.network

import com.mezri.bigburger.utils.API_BASE_PATH
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object RetrofitClient {

    private val retrofitClient by lazy {
        Retrofit.Builder()
            .baseUrl(API_BASE_PATH)
            .addConverterFactory(HttpClient.getConverterFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(HttpClient.getHttpClient())
            .build()
    }

    /**
     * Return retrofit network client
     */
    fun getNetworkClient(): Retrofit = retrofitClient
}