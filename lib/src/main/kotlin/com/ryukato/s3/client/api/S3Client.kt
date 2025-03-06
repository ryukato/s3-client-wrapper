package com.ryukato.s3.client.api

import com.ryukato.s3.client.model.StorageBucket
import com.ryukato.s3.client.model.StorageItem

interface S3Client {
    suspend fun createBucket(request: CreateBucketRequest): StorageBucket?
    suspend fun fetchBuckets(): Collection<StorageBucket>
    suspend fun fetchObjectsInBucket(request: FetchObjectInBucketRequest): Collection<StorageItem>
    suspend fun uploadImageFileItemToBucket(request: UploadImageFileRequest): StorageItem
    suspend fun uploadImageStreamItemToBucket(request: UploadImageStreamRequest): StorageItem
    suspend fun getTemporaryUrl(request: GetTemporaryUrlRequest): String?
}
