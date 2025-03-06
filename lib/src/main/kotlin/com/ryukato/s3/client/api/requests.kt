package com.ryukato.s3.client.api

import java.io.InputStream

data class CreateBucketRequest(
    val bucketName: String,
    val doObjectLock: Boolean = false,
    val extraQueryParams: Map<String, String> = emptyMap(),
    val extraHeaders: Map<String, String> = emptyMap(),
)

data class UploadImageFileRequest(
    val bucketName: String,
    val itemName: String,
    val itemFilePath: String,
    val uploadedBy: String,
)

data class UploadImageStreamRequest(
    val bucketName: String,
    val itemName: String,
    val itemType: String,
    val itemStream: InputStream,
    val uploadedBy: String,
) {
    override fun toString(): String {
        return "UploadImageStreamRequest(bucketName='$bucketName', itemName='$itemName', itemType='$itemType', uploadedBy='$uploadedBy')"
    }
}

data class GetTemporaryUrlRequest(
    val bucketName: String,
    val itemName: String,
    val expiryInHours: Int,
    val contentType: String
)

data class FetchObjectInBucketRequest(
    val bucketName: String,
    val maxSize: Int = 100,
)
