package com.ryukato.s3.client

import java.time.Duration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class HttpClientFactory {
    fun httpClient(
        enableLogging: Boolean,
    ): OkHttpClient {
        val logLevel =
            if (!enableLogging) HttpLoggingInterceptor.Level.NONE else HttpLoggingInterceptor.Level.BODY
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(logLevel)

        return OkHttpClient.Builder()
            .callTimeout(Duration.ofMinutes(5))
            .connectTimeout(Duration.ofMinutes(10))
            .readTimeout(Duration.ofMinutes(5))
            .addInterceptor(loggingInterceptor).build()
    }
}
