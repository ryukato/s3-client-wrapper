package com.ryukato.s3.client.minio

import com.ryukato.s3.client.api.CreateBucketRequest
import com.ryukato.s3.client.api.FetchObjectInBucketRequest
import com.ryukato.s3.client.api.UploadImageFileRequest
import com.ryukato.s3.client.util.getTestResourceFile
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MinioClientWrapperTest : AbstractMinioIntegrationTest() {

    @Test
    fun testAllFunctions() = runBlocking {
        val testBucketName = "test"
        assertNotNull(minioContainer.containerId)
        val properties = MinioClientProperties(
            endpoint = minioContainer.s3URL,
            accessKey = minioContainer.userName,
            secretKey = minioContainer.password,
        )
        val client = MinioClientWrapper(MinioClientFactory(properties))
        assertNotNull(client)
        val createdBucket = client.createBucket(CreateBucketRequest(bucketName = testBucketName))
        assertNotNull(createdBucket)
        assertEquals(testBucketName, createdBucket!!.name)
        val buckets = client.fetchBuckets()
        assertNotNull(buckets)
        assertTrue(buckets.isNotEmpty())

        val testImageResource = getTestResourceFile("test-images/ia-forrest.png", this::class.java.classLoader)
        val createdStorageItem = client.uploadImageFileItemToBucket(
            request = UploadImageFileRequest(
                bucketName = testBucketName,
                itemName = "test",
                itemFilePath = testImageResource.absolutePath,
                uploadedBy = "test"
            )
        )
        assertNotNull(createdStorageItem)
        assertEquals("test", createdStorageItem.name)

        val bucketItems = client.fetchObjectsInBucket(
            request = FetchObjectInBucketRequest(
                bucketName = testBucketName
            )
        )
        assertTrue(bucketItems.any { it.name == "test" })
    }
}
