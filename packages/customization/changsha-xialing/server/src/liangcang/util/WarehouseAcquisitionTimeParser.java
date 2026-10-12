package com.guhecloud.cube.customization.xialing.server.liangcang.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** 智慧仓库 acquisitionDate：秒/毫秒戳或 ISO 串 → LocalDateTime。 */
public final class WarehouseAcquisitionTimeParser {

    private WarehouseAcquisitionTimeParser() {
    }

    public static LocalDateTime parse(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        ZoneId zone = ZoneId.systemDefault();
        if (node.isNumber()) {
            long v = node.asLong();
            if (v < 1_000_000_000_000L) {
                return LocalDateTime.ofInstant(Instant.ofEpochSecond(v), zone);
            }
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(v), zone);
        }
        if (node.isTextual()) {
            String t = node.asText().trim();
            if (t.isEmpty()) {
                return null;
            }
            try {
                return LocalDateTime.parse(t, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (Exception ignored) {
                try {
                    return ZonedDateTime.parse(t, DateTimeFormatter.ISO_OFFSET_DATE_TIME).withZoneSameInstant(zone).toLocalDateTime();
                } catch (Exception ignored2) {
                    try {
                        return ZonedDateTime.parse(t, DateTimeFormatter.ISO_ZONED_DATE_TIME).withZoneSameInstant(zone).toLocalDateTime();
                    } catch (Exception ignored3) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
