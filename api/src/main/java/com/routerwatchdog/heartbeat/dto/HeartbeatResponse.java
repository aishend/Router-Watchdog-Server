package com.routerwatchdog.heartbeat.dto;

import com.routerwatchdog.commands.CommandType;

public record HeartbeatResponse(
        boolean success,
        CommandType command
) {
}