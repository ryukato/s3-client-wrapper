package com.ryukato.s3.client.util

import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.fail

suspend fun getTestResourceFile(resourcePath: String, classLoader: ClassLoader?): File {
    return withContext(Dispatchers.IO) {
        if (classLoader != null) {
            classLoader.getResource(resourcePath)?.toURI()?.let { File(it) }
                ?: fail { "fail to get test-resource file" }
        } else {
            this::class.java.classLoader.getResource(resourcePath)?.toURI()?.let { File(it) }
                ?: fail { "fail to get test-resource file" }
        }
    }
}
