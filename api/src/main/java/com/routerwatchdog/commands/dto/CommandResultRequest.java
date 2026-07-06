package com.routerwatchdog.commands.dto;

import com.routerwatchdog.commands.CommandStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommandResultRequest(
        @NotBlank
        String commandId,

        @NotNull
        CommandStatus status
) {
}