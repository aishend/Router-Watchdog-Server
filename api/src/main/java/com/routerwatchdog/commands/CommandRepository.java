package com.routerwatchdog.commands;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class CommandRepository {
    private final ConcurrentMap<String, PendingCommand> commandsById = new ConcurrentHashMap<>();

    public void save(PendingCommand command) {
        commandsById.put(command.id(), command);
    }

    public PendingCommand findById(String commandId) {
        return commandsById.get(commandId);
    }

    public PendingCommand update(PendingCommand command) {
        commandsById.put(command.id(), command);
        return command;
    }

    public Collection<PendingCommand> findAll() {
        return commandsById.values().stream()
                .sorted(Comparator.comparing(
                        (PendingCommand command) -> command.createdAt()).reversed())
                .toList();
    }
}