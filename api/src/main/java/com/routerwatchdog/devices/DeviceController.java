package com.routerwatchdog.devices;

import com.routerwatchdog.devices.dto.DeviceResponse;
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

                    return new DeviceResponse(
                            device.request().deviceId(),
                            isDown ? "DOWN" : "UP",
                            device.request().ip(),
                            device.request().gateway(),
                            device.request().failures(),
                            device.request().uptime(),
                            device.request().rssi(),
                            device.request().freeHeap(),
                            device.request().firmwareVersion(),
                            device.lastReceivedAt(),
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