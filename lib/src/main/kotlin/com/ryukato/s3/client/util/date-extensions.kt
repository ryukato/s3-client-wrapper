package com.ryukato.s3.client.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

val DEFAULT_ZONE_ID: ZoneId = ZoneId.of("Asia/Seoul")

fun LocalDateTime.toEpochMilli(zoneId: ZoneId = DEFAULT_ZONE_ID): Long {
    return this.truncatedTo(ChronoUnit.MILLIS).atZone(zoneId).toInstant().toEpochMilli()
}

fun LocalDate.toEpochMilli(zoneId: ZoneId = DEFAULT_ZONE_ID): Long {
    return this.atStartOfDay(zoneId).toInstant().toEpochMilli();
}
