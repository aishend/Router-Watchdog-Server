package com.routerwatchdog.commands;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Component
public class CommandQueue {
    private final ConcurrentMap<String, Queue<PendingCommand>> queuedCommandsByDevice = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, PendingCommand> commandsById = new ConcurrentHashMap<>();

    public PendingCommand queueCommand(String deviceId, CommandType commandType) {
        if (commandType == CommandType.NONE) {
            return null;
        }

        PendingCommand command = PendingCommand.create(commandType);

        queuedCommandsByDevice
                .computeIfAbsent(deviceId, ignored -> new ConcurrentLinkedQueue<>())
                .add(command);

        commandsById.put(command.id(), command);

        return command;
    }

    public PendingCommand pollCommand(String deviceId) {
        Queue<PendingCommand> commands = queuedCommandsByDevice.get(deviceId);

        if (commands == null) {
            return null;
        }

        PendingCommand command = commands.poll();

        if (command == null) {
            return null;
        }

        PendingCommand deliveredCommand = command.markDelivered();
        commandsById.put(deliveredCommand.id(), deliveredCommand);

        return deliveredCommand;
    }

    public PendingCommand completeCommand(String commandId) {
        PendingCommand command = commandsById.get(commandId);

        if (command == null) {
            return null;
        }

        PendingCommand completedCommand = command.markCompleted();
        commandsById.put(commandId, completedCommand);

        return completedCommand;
    }

    public Collection<PendingCommand> getCommands() {
        return commandsById.values();
    }

    public PendingCommand failCommand(String commandId) {
        PendingCommand command = commandsById.get(commandId);

        if (command == null) {
            return null;
        }

        PendingCommand failedCommand = command.markFailed();
        commandsById.put(commandId, failedCommand);

        return failedCommand;
    }
}