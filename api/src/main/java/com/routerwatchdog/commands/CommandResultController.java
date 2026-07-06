package com.routerwatchdog.commands;

import com.routerwatchdog.commands.dto.CommandResultRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/command-results")
public class CommandResultController {
    private final CommandQueue commandQueue;

    public CommandResultController(CommandQueue commandQueue) {
        this.commandQueue = commandQueue;
    }

    @PostMapping
    public ResponseEntity<?> receiveCommandResult(
            @Valid @RequestBody CommandResultRequest request) {
        if (request.status() != CommandStatus.COMPLETED &&
                request.status() != CommandStatus.FAILED) {
            return ResponseEntity.badRequest().body("Only COMPLETED or FAILED are valid result statuses");
        }

        PendingCommand command = switch (request.status()) {
            case COMPLETED -> commandQueue.completeCommand(request.commandId());
            case FAILED -> commandQueue.failCommand(request.commandId());
            default -> null;
        };

        if (command == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(command);
    }
}