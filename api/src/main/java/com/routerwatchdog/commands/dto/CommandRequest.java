package com.routerwatchdog.commands.dto;

import com.routerwatchdog.commands.CommandType;
import jakarta.validation.constraints.NotNull;

public record CommandRequest(
        @NotNull
        CommandType command
) {
}