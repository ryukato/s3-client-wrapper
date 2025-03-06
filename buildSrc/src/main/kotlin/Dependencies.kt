import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec

object Versions {
    const val kotlin = "1.9.21"
    const val kotlinxCoroutine = "1.7.3"
    const val kotlinLogging = "3.0.5"

    const val jasypt = "3.0.5"
    const val junit = "5.10.1"
    const val commonIo = "2.18.0"
    const val s3 = "2.25.7"
    const val minio = "8.5.17"
    const val okhttp = "4.12.0"

    const val testContainerVersion = "1.17.2"
    const val testMinioContainerVersion = "1.20.6"
}

object ExtLibs {
    // kotlin
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect"
    const val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3"
    const val kotlinxCoroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutine}"
    const val kotlinxCoroutinesCoreJvm =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${Versions.kotlinxCoroutine}"
    const val kotlinCoroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinxCoroutine}"

    // jasypt
    const val jasypt = "com.github.ulisesbocchio:jasypt-spring-boot-starter:${Versions.jasypt}"

    const val kotlinLogging = "io.github.microutils:kotlin-logging:${Versions.kotlinLogging}"

    const val junit = "org.junit.jupiter:junit-jupiter:${Versions.junit}"
    const val commonIo = "commons-io:commons-io:${Versions.commonIo}"

    // s3
    const val s3 = "software.amazon.awssdk:s3:${Versions.s3}"
    const val minio = "io.minio:minio:${Versions.minio}"

    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"

    const val testContainer = "org.testcontainers:testcontainers:${Versions.testContainerVersion}"
    const val testMinioTestContainer = "org.testcontainers:minio:${Versions.testMinioContainerVersion}"
}

