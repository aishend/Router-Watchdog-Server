package com.routerwatchdog.commands;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/commands")
public class CommandQueryController {

    private final CommandRepository commandRepository;

    public CommandQueryController(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    @GetMapping
    public ResponseEntity<?> getCommands() {
        return ResponseEntity.ok(commandRepository.findAll());
    }
}