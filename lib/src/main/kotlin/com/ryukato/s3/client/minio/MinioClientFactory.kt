package com.ryukato.s3.client.minio

import  com.ryukato.s3.client.HttpClientFactory
import io.minio.MinioClient

class MinioClientFactory(
    private val properties: MinioClientProperties
) {
    private val httpClientFactory = HttpClientFactory()

    fun createMinioClient(): MinioClient {
        val minioClient = if (cache.containsKey(properties.endpoint)) {
            cache[properties.endpoint] ?: createNew()
        } else {
            createNew()
        }
        cache[properties.endpoint] = minioClient
        return minioClient
    }

    private fun createNew(): MinioClient {
        val httpClient = httpClientFactory.httpClient(properties.enableLogging)
        return MinioClient.builder().httpClient(httpClient)
            .endpoint(properties.endpoint)
            .credentials(properties.accessKey, properties.secretKey)
            .build()
    }

    companion object {
        private val cache = mutableMapOf<String, MinioClient>()
    }
}
