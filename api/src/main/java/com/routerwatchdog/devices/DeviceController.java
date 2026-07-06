package com.routerwatchdog.devices;

import com.routerwatchdog.devices.dto.DeviceResponse;
import com.routerwatchdog.devices.dto.UpdateDeviceMetadataRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

    private final WatchdogDeviceService watchdogDeviceService;

    public DeviceController(WatchdogDeviceService watchdogDeviceService) {
        this.watchdogDeviceService = watchdogDeviceService;
    }

    @GetMapping
    public ResponseEntity<?> getDevices() {
        Instant now = Instant.now();

        var devices = watchdogDeviceService.getDevicesByArrivalOrder().stream()
                .map(device -> toResponse(device, now))
                .toList();

        return ResponseEntity.ok(
                Map.of(
                        "devices", devices,
                        "serverTime", now
                )
        );
    }

    @PatchMapping("/{deviceId}/metadata")
    public ResponseEntity<?> updateMetadata(
            @PathVariable String deviceId,
            @Valid @RequestBody UpdateDeviceMetadataRequest request
    ) {
        WatchdogDeviceEntity device = watchdogDeviceService.updateMetadata(deviceId, request);

        return ResponseEntity.ok(
                toResponse(device, Instant.now())
        );
    }

    private DeviceResponse toResponse(WatchdogDeviceEntity device, Instant now) {
        long secondsSinceLastHeartbeat =
                Duration.between(device.getLastReceivedAt(), now).toSeconds();

        boolean isDown = secondsSinceLastHeartbeat > 30;

        return new DeviceResponse(
                device.getDeviceId(),
                device.getDisplayName(),
                device.getLocation(),
                device.getNotes(),
                device.isEnabled(),
                isDown ? "DOWN" : "UP",
                device.getIp(),
                device.getGateway(),
                device.getFailures(),
                device.getUptime(),
                device.getRssi(),
                device.getFreeHeap(),
                device.getFirmwareVersion(),
                device.getFirstSeenAt(),
                device.getLastReceivedAt(),
                secondsSinceLastHeartbeat
        );
    }
}