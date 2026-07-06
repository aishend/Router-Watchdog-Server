package com.routerwatchdog.networkclients.dto;

import java.time.Instant;

public record NetworkClientResponse(
        String id,
        String watchdogDeviceId,
        String ip,
        String mac,
        String hostname,
        String vendor,
        Instant firstSeenAt,
        Instant lastSeenAt
) {
}
