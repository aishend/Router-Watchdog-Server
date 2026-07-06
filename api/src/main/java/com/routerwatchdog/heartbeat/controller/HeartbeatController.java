package com.routerwatchdog.heartbeat.controller;

import com.routerwatchdog.commands.CommandQueue;
import com.routerwatchdog.commands.PendingCommand;
import com.routerwatchdog.devices.WatchdogDeviceService;
import com.routerwatchdog.heartbeat.dto.HeartbeatRequest;
import com.routerwatchdog.heartbeat.dto.HeartbeatResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/heartbeat")
public class HeartbeatController {

    private final CommandQueue commandQueue;
    private final WatchdogDeviceService watchdogDeviceService;

    public HeartbeatController(
            CommandQueue commandQueue,
            WatchdogDeviceService watchdogDeviceService
    ) {
        this.commandQueue = commandQueue;
        this.watchdogDeviceService = watchdogDeviceService;
    }

    @PostMapping
    public ResponseEntity<HeartbeatResponse> receiveHeartbeat(
            @Valid @RequestBody HeartbeatRequest request
    ) {
        Instant receivedAt = Instant.now();

        System.out.println("Heartbeat received");
        System.out.println(request);

        watchdogDeviceService.recordHeartbeat(request, receivedAt);

        PendingCommand command = commandQueue.pollCommand(request.deviceId());

        if (command == null) {
            return ResponseEntity.ok(HeartbeatResponse.noCommand());
        }

        return ResponseEntity.ok(
                HeartbeatResponse.withCommand(command.id(), command.type())
        );
    }
}