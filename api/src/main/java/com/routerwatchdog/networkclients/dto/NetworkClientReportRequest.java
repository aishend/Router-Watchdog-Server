package com.routerwatchdog.networkclients.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record NetworkClientReportRequest(
        @NotBlank String watchdogDeviceId,
        @NotEmpty List<@Valid NetworkClientItemRequest> clients
) {
}
