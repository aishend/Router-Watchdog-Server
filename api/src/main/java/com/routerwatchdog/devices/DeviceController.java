package com.routerwatchdog.devices;

import com.routerwatchdog.devices.dto.DeviceResponse;
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
                .map(device -> {
                    long secondsSinceLastHeartbeat =
                            Duration.between(device.getLastReceivedAt(), now).toSeconds();

                    boolean isDown = secondsSinceLastHeartbeat > 30;

                    return new DeviceResponse(
                            device.getDeviceId(),
                            isDown ? "DOWN" : "UP",
                            device.getIp(),
                            device.getGateway(),
                            device.getFailures(),
                            device.getUptime(),
                            device.getRssi(),
                            device.getFreeHeap(),
                            device.getFirmwareVersion(),
                            device.getLastReceivedAt(),
                            secondsSinceLastHeartbeat
                    );
                })
                .toList();

        return ResponseEntity.ok(
                Map.of(
                        "devices", devices,
                        "serverTime", now
                )
        );
    }
}