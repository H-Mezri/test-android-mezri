package com.mezri.bigburger.utils

// API params
const val API_BASE_PATH = "https://bigburger.useradgents.com"

// OkHttp client params
const val CACHE_SIZE = (10 * 1024 * 1024).toLong() // 10 megabytes

// the request response will be available for one minute without resending a new request
const val CACHE_MAX_AGE = 60 // in seconds
