package com.routerwatchdog.devices;

import com.routerwatchdog.heartbeat.HeartbeatState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {
    private final HeartbeatState heartbeatState;

    public DeviceController(HeartbeatState heartbeatState) {
        this.heartbeatState = heartbeatState;
    }

    @GetMapping
    public ResponseEntity<?> getDevices() {
        Instant now = Instant.now();

        var devices = heartbeatState.getDevices().stream()
                .map(device -> {
                    long secondsSinceLastHeartbeat =
                            Duration.between(device.lastReceivedAt(), now).toSeconds();

                    boolean isDown = secondsSinceLastHeartbeat > 30;

                    return Map.<String, Object>of(
                            "deviceId", device.request().deviceId(),
                            "deviceStatus", isDown ? "DOWN" : "UP",
                            "ip", device.request().ip(),
                            "gateway", device.request().gateway(),
                            "failures", device.request().failures(),
                            "uptime", device.request().uptime(),
                            "lastReceivedAt", device.lastReceivedAt(),
                            "secondsSinceLastHeartbeat", secondsSinceLastHeartbeat
                    );
                })
                .toList();

        return ResponseEntity.ok(
                Map.of(
                        "devices", devices,
                        "serverTime", now
                ));
    }
}