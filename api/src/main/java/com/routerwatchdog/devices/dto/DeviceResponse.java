package com.routerwatchdog.devices.dto;

import java.time.Instant;

public record DeviceResponse(
        String deviceId,
        String displayName,
        String location,
        String notes,
        boolean enabled,
        String deviceStatus,
        String ip,
        String gateway,
        int failures,
        long uptime,
        Integer rssi,
        long freeHeap,
        String firmwareVersion,
        Instant firstSeenAt,
        Instant lastReceivedAt,
        long secondsSinceLastHeartbeat
) {
}