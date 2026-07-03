package com.leandro.routerwatchdog.heartbeat.controller;

import com.leandro.routerwatchdog.heartbeat.HeartbeatState;
import com.leandro.routerwatchdog.heartbeat.dto.HeartbeatRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import java.time.Instant;

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
    Instant lastReceivedAt = heartbeatState.getLastReceivedAt();

    if (lastReceivedAt == null) {
        long secondsSinceServerStart =
                Duration.between(heartbeatState.getServerStartedAt(), now).toSeconds();

        return ResponseEntity.ok(
                Map.of(
                        "deviceStatus", secondsSinceServerStart >= 30 ? "DOWN" : "WAITING",
                        "lastRequest", null,
                        "lastReceivedAt", null,
                        "secondsSinceLastHeartbeat", secondsSinceServerStart,
                        "serverTime", now
                )
        );
    }

    long secondsSinceLastHeartbeat =
            Duration.between(lastReceivedAt, now).toSeconds();

    boolean isDown = secondsSinceLastHeartbeat > 30;

    return ResponseEntity.ok(
            Map.of(
                    "deviceStatus", isDown ? "DOWN" : "UP",
                    "lastRequest", heartbeatState.getLastRequest(),
                    "lastReceivedAt", lastReceivedAt,
                    "secondsSinceLastHeartbeat", secondsSinceLastHeartbeat,
                    "serverTime", now
            )
    );
}
}
