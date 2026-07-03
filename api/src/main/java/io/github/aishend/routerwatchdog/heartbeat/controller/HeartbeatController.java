package io.github.aishend.routerwatchdog.heartbeat.controller;

import io.github.aishend.routerwatchdog.heartbeat.HeartbeatState;
import io.github.aishend.routerwatchdog.heartbeat.dto.HeartbeatRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/heartbeat")
public class HeartbeatController {

    private final HeartbeatState heartbeatState;

    public HeartbeatController(HeartbeatState heartbeatState) {
        this.heartbeatState = heartbeatState;
    }

    @PostMapping
    public ResponseEntity<Map<String, Boolean>> receiveHeartbeat(
            @Valid @RequestBody HeartbeatRequest request) {
        System.out.println("Heartbeat received");
        System.out.println(request);

        heartbeatState.save(request);

        return ResponseEntity.ok(
                Map.of("success", true));
    }

    @GetMapping("/latest")
    public ResponseEntity<Map<String, Object>> getLatestHeartbeat() {
        Instant now = Instant.now();

        List<Map<String, Object>> devices = heartbeatState.getDevices().stream()
                .map(device -> {
                    long secondsSinceLastHeartbeat = Duration.between(device.lastReceivedAt(), now).toSeconds();
                    boolean isDown = secondsSinceLastHeartbeat > 30;

                    return Map.<String, Object>of(
                            "deviceId", device.request().deviceId(),
                            "deviceStatus", isDown ? "DOWN" : "UP",
                            "lastReceivedAt", device.lastReceivedAt(),
                            "secondsSinceLastHeartbeat", secondsSinceLastHeartbeat,
                            "minutesSinceLastHeartbeat", Math.max(1, secondsSinceLastHeartbeat / 60));
                })
                .toList();

        return ResponseEntity.ok(
                Map.of(
                        "devices", devices,
                        "serverTime", now));
    }
}
