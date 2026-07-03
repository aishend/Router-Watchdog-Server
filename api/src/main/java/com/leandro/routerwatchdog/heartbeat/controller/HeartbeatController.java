package com.leandro.routerwatchdog.heartbeat.controller;

import com.leandro.routerwatchdog.heartbeat.dto.HeartbeatRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/heartbeat")
public class HeartbeatController {

    @PostMapping
    public ResponseEntity<Map<String, Boolean>> receiveHeartbeat(
            @Valid @RequestBody HeartbeatRequest request
    ) {

        System.out.println("Heartbeat received");
        System.out.println(request);

        return ResponseEntity.ok(
                Map.of("success", true)
        );
    }
}