package com.routerwatchdog.commands;

import com.routerwatchdog.commands.dto.CommandRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/commands")
public class CommandController {
    private final CommandQueue commandQueue;

    public CommandController(CommandQueue commandQueue) {
        this.commandQueue = commandQueue;
    }

    @PostMapping("/{deviceId}")
    public ResponseEntity<Map<String, Object>> queueCommand(
            @PathVariable String deviceId,
            @Valid @RequestBody CommandRequest request) {
        PendingCommand command = commandQueue.queueCommand(deviceId, request.command());

        System.out.println("========== CREATE COMMAND ==========");
        System.out.println(request);
        System.out.println("Device: " + deviceId);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "deviceId", deviceId,
                        "command", command));
    }
}