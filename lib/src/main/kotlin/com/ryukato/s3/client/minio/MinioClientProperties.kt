package com.ryukato.s3.client.minio

data class MinioClientProperties(
    val endpoint: String,
    val accessKey: String,
    val secretKey: String,
    val enableLogging: Boolean = false
)
