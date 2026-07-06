package com.routerwatchdog.heartbeat.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HeartbeatRequest(
        @NotBlank
        String deviceId,

        @NotBlank
        String ip,

        @NotBlank
        String gateway,

        @Min(0)
        int failures,

        @Min(0)
        long uptime,

        @NotNull
        Integer rssi,

        @Min(0)
        long freeHeap,

        @NotBlank
        String firmwareVersion
) {
}