package com.ryukato.s3.client.model

import java.time.LocalDateTime
import com.ryukato.s3.client.util.toEpochMilli

data class StorageItem(
    val name: String,
    val size: Long,
    val createdBy: String,
    val etag: String = "",
    val lastModified: Long,
    val isEmpty: Boolean = false,
    val description: String = ""
) {
    companion object {
        fun createEmptyItem(name: String = "empty", reason: String = ""): StorageItem {
            return StorageItem(
                isEmpty = true,
                name = name,
                size = 0L,
                createdBy = "none",
                description = reason,
                lastModified = LocalDateTime.now().toEpochMilli()
            )
        }

        fun createNormal(
            name: String,
            size: Long,
            createdBy: String,
            etag: String = "",
            lastModified: Long = LocalDateTime.now().toEpochMilli(),
        ): StorageItem {
            return StorageItem(
                name = name,
                size = size,
                etag = etag,
                createdBy = createdBy,
                lastModified = lastModified
            )
        }
    }
}
