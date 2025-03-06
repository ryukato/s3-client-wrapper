package com.ryukato.s3.client.minio

import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.MinIOContainer

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractMinioIntegrationTest {
    companion object {

        val minioContainer: MinIOContainer = MinIOContainer("minio/minio:RELEASE.2023-09-04T19-57-37Z")
            .withPassword("MinioTest2025!@")
            .withUserName("test")

        init {
            minioContainer.start()
        }
    }
}
