package com.ryukato.s3.client.minio

import com.ryukato.s3.client.api.CreateBucketRequest
import com.ryukato.s3.client.api.FetchObjectInBucketRequest
import com.ryukato.s3.client.api.GetTemporaryUrlRequest
import com.ryukato.s3.client.api.S3Client
import com.ryukato.s3.client.api.UploadImageFileRequest
import com.ryukato.s3.client.api.UploadImageStreamRequest
import com.ryukato.s3.client.model.StorageBucket
import com.ryukato.s3.client.model.StorageItem
import com.ryukato.s3.client.util.toEpochMilli
import io.minio.GetPresignedObjectUrlArgs
import io.minio.ListObjectsArgs
import io.minio.MakeBucketArgs
import io.minio.PutObjectArgs
import io.minio.UploadObjectArgs
import io.minio.http.Method
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Path
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.apache.commons.io.IOUtils

private val logger = KotlinLogging.logger {}

class MinioClientWrapper(
    private val minioClientFactory: MinioClientFactory
) : S3Client {
    private val delegateClient = minioClientFactory.createMinioClient()

    override suspend fun createBucket(request: CreateBucketRequest): StorageBucket? {
        val args = MakeBucketArgs.builder()
            .bucket(request.bucketName)
            .objectLock(request.doObjectLock)
            .extraQueryParams(request.extraQueryParams)
            .extraHeaders(request.extraHeaders)
            .build()
        return withContext(Dispatchers.IO) {
            try {
                delegateClient.makeBucket(args)
                StorageBucket(name = request.bucketName, createdAt = LocalDateTime.now().toEpochMilli())
            } catch (_: Exception) {
                null
            }
        }
    }

    override suspend fun fetchBuckets(): Collection<StorageBucket> {
        return try {
            withContext(Dispatchers.IO) {
                delegateClient.listBuckets().map {
                    StorageBucket(
                        name = it.name(),
                        createdAt = it.creationDate().toLocalDateTime().toEpochMilli()
                    )
                }
            }
        } catch (e: Exception) {
            logger.error(e) { "Fail to fetch list of buckets, cause: ${e.message}" }
            emptyList()
        }
    }

    override suspend fun fetchObjectsInBucket(request: FetchObjectInBucketRequest): Collection<StorageItem> {
        val args = ListObjectsArgs.builder()
            .bucket(request.bucketName)
            .maxKeys(request.maxSize)
            .build()

        return withContext(Dispatchers.IO) {
            delegateClient.listObjects(args).map {
                try {
                    val item = it.get()
                    val owner = item.owner()
                    StorageItem(
                        name = item.objectName(),
                        size = item.size(),
                        createdBy = if (owner != null) owner.id() else "none",
                        etag = item.etag(),
                        lastModified = item.lastModified().toLocalDateTime().toEpochMilli()
                    )
                } catch (e: Exception) {
                    logger.error(e) { "Fail to fetch objects in the bucket - request: $request, cause: ${e.message}" }
                    StorageItem.createEmptyItem(reason = e.message ?: "")
                }
            }
        }
    }

    override suspend fun uploadImageFileItemToBucket(
        request: UploadImageFileRequest
    ): StorageItem {
        val itemFile = File(Path.of(request.itemFilePath).toUri())
        return if (!itemFile.exists() || !itemFile.canRead()) {
            StorageItem.createEmptyItem(
                reason = "Fail to upload - the item file is not accessible"
            )
        } else {
            val contentType = "image/${itemFile.extension}"
            val uploadObjectArgs = UploadObjectArgs.builder()
                .bucket(request.bucketName)
                .`object`(request.itemName)
                .contentType(contentType)
                .filename(request.itemFilePath)
                .build()
            try {
                val uploadResponse = withContext(Dispatchers.IO) {
                    delegateClient.uploadObject(uploadObjectArgs)
                }
                StorageItem.createNormal(
                    name = uploadResponse.`object`(),
                    size = itemFile.length(),
                    etag = uploadResponse.etag(),
                    createdBy = request.uploadedBy,
                )
            } catch (e: Exception) {
                logger.error(e) { "Fail to upload object, request:${request}, cause: ${e.message}" }
                StorageItem.createEmptyItem(
                    reason = "Fail to upload - cause: ${e.message}"
                )
            }
        }
    }

    override suspend fun uploadImageStreamItemToBucket(
        request: UploadImageStreamRequest
    ): StorageItem {
        val outputStream = ByteArrayOutputStream()
        IOUtils.copy(request.itemStream, outputStream)
        val size = outputStream.size()
        return if (size == 0) {
            StorageItem.createEmptyItem(
                reason = "Fail to upload - the item stream looks empty"
            )
        } else {
            val contentType = "image/${request.itemType}"
            val putObjectArgs = PutObjectArgs.builder()
                .bucket(request.bucketName)
                .`object`(request.itemName)
                .contentType(contentType)
                .stream(request.itemStream, 0, -1)
                .build()
            try {
                val uploadResponse = withContext(Dispatchers.IO) {
                    delegateClient.putObject(putObjectArgs)
                }
                StorageItem.createNormal(
                    name = uploadResponse.`object`(),
                    size = size.toLong(),
                    etag = uploadResponse.etag(),
                    createdBy = request.uploadedBy,
                )
            } catch (e: Exception) {
                logger.error(e) { "Fail to upload object, request:${request}, cause: ${e.message}" }
                StorageItem.createEmptyItem(
                    reason = "Fail to upload - cause: ${e.message}"
                )
            }
        }
    }

    override suspend fun getTemporaryUrl(
        request: GetTemporaryUrlRequest
    ): String? {
        val reqParams = mapOf(
            "response-content-type" to request.contentType
        )
        val getPreSignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
            .method(Method.GET)
            .bucket(request.bucketName)
            .`object`(request.itemName)
            .expiry(request.expiryInHours, TimeUnit.HOURS)
            .extraQueryParams(reqParams)
            .build()
        return try {
            withContext(Dispatchers.IO) {
                delegateClient.getPresignedObjectUrl(getPreSignedObjectUrlArgs)
            }
        } catch (e: Exception) {
            logger.error(e) { "Fail to get url of the object, request:${request}, cause: ${e.message}" }
            null
        }
    }
}

