package com.leandro.routerwatchdog.heartbeat.controller;

import com.leandro.routerwatchdog.heartbeat.HeartbeatState;
import com.leandro.routerwatchdog.heartbeat.dto.HeartbeatRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @Valid @RequestBody HeartbeatRequest request
    ) {
        System.out.println("Heartbeat received");
        System.out.println(request);

        heartbeatState.save(request);

        return ResponseEntity.ok(
                Map.of("success", true)
        );
    }
    @GetMapping("/latest")
    public ResponseEntity<Map<String, Object>> getLatestHeartbeat() {
        return ResponseEntity.ok(
                Map.of(
                        "lastRequest", heartbeatState.getLastRequest(),
                        "lastReceivedAt", heartbeatState.getLastReceivedAt()
                )
        );
    }
    }

