package com.routerwatchdog.devices.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateDeviceMetadataRequest(
        @NotBlank String displayName,
        String location,
        String notes,
        boolean enabled
) {
}