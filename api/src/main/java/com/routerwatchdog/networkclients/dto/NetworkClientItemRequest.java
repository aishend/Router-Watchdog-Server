package com.routerwatchdog.networkclients.dto;

import jakarta.validation.constraints.NotBlank;

public record NetworkClientItemRequest(
        @NotBlank String ip,
        @NotBlank String mac,
        String hostname,
        String vendor
) {
}
