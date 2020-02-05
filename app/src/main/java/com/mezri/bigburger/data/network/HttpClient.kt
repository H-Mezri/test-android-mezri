package com.mezri.bigburger.data.network

import com.mezri.bigburger.utils.CACHE_MAX_AGE
import com.mezri.bigburger.utils.CACHE_SIZE
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

private const val HEADER_CACHE_NAME = "Cache-Control"
private const val HEADER_CACHE_VALUE = "public, max-age="

object HttpClient {

    // Ok Http client
    private var client: OkHttpClient? = null

    // Gson converter
    private var converterFactory: MoshiConverterFactory? = null

    // Ok Http cache
    private lateinit var okHttpCache: Cache

    fun initCacheDirectory(cacheDir: File) {
        okHttpCache = Cache(cacheDir, CACHE_SIZE)
    }

    /**
     * Initialize OkHttp client and return it
     */
    fun getHttpClient(): OkHttpClient {
        if (client == null) {
            val httpBuilder = OkHttpClient.Builder()
            httpBuilder
                .cache(okHttpCache)
                .addInterceptor {
                    // get the current request
                    var request = it.request()

                    // set header to check if there is a cache not older than 60 seconds in this case
                    request = request.newBuilder()
                        .header(HEADER_CACHE_NAME, HEADER_CACHE_VALUE + CACHE_MAX_AGE)
                        .build()

                    // process request
                    it.proceed(request)
                }

            client = httpBuilder.build()
        }
        return client!!
    }

    /**
     * Initialize Converter Factory and return it
     */
    fun getConverterFactory(): MoshiConverterFactory {
        if (converterFactory == null) {
            converterFactory = MoshiConverterFactory.create()
        }
        return converterFactory!!
    }
}

