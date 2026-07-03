package com.routerwatchdog.heartbeat.dto;

import com.routerwatchdog.commands.PendingCommand;

public record HeartbeatResponse(
                boolean success,
                PendingCommand command) {
}