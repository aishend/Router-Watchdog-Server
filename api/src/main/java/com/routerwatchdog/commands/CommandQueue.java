package com.routerwatchdog.commands;

import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Component
public class CommandQueue {
    private final ConcurrentMap<String, Queue<String>> queuedCommandIdsByDevice = new ConcurrentHashMap<>();
    private final CommandRepository commandRepository;

    public CommandQueue(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public PendingCommand queueCommand(String deviceId, CommandType commandType) {
        if (commandType == CommandType.NONE) {
            return null;
        }

        PendingCommand command = PendingCommand.create(commandType);

        commandRepository.save(command);

        queuedCommandIdsByDevice
                .computeIfAbsent(deviceId, ignored -> new ConcurrentLinkedQueue<>())
                .add(command.id());

        return command;
    }

    public PendingCommand pollCommand(String deviceId) {
        Queue<String> commandIds = queuedCommandIdsByDevice.get(deviceId);

        if (commandIds == null) {
            return null;
        }

        String commandId = commandIds.poll();

        if (commandId == null) {
            return null;
        }

        PendingCommand command = commandRepository.findById(commandId);

        if (command == null) {
            return null;
        }

        PendingCommand deliveredCommand = command.markDelivered();

        return commandRepository.update(deliveredCommand);
    }

    public PendingCommand completeCommand(String commandId) {
        PendingCommand command = commandRepository.findById(commandId);

        if (command == null) {
            return null;
        }

        return commandRepository.update(command.markCompleted());
    }

    public PendingCommand failCommand(String commandId) {
        PendingCommand command = commandRepository.findById(commandId);

        if (command == null) {
            return null;
        }

        return commandRepository.update(command.markFailed());
    }
}