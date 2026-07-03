package com.routerwatchdog.heartbeat.dto;

public record HeartbeatResponse(
        boolean success,
        String command
) {
}