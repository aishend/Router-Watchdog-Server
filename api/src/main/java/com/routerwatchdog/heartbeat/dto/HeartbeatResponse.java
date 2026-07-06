package com.routerwatchdog.heartbeat.dto;

import com.routerwatchdog.commands.CommandType;

public record HeartbeatResponse(
        PendingCommandResponse command
) {
    public static HeartbeatResponse noCommand() {
        return new HeartbeatResponse(null);
    }

    public static HeartbeatResponse withCommand(String commandId, CommandType commandType) {
        return new HeartbeatResponse(
                new PendingCommandResponse(commandId, commandType)
        );
    }

    public record PendingCommandResponse(
            String id,
            CommandType type
    ) {
    }
}