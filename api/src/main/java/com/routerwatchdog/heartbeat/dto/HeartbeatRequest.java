package com.routerwatchdog.heartbeat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

public record HeartbeatRequest(
                @NotBlank String deviceId,

                @NotBlank String ip,

                @NotBlank String gateway,

                @Min(0) int failures,

                @Min(0) long uptime) {
}
