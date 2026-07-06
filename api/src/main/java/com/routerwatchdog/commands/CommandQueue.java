package com.routerwatchdog.commands;

import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Component
public class CommandQueue {

    private final ConcurrentMap<String, Queue<String>> queuedCommandIdsByDevice =
            new ConcurrentHashMap<>();

    private final CommandRepository commandRepository;

    public CommandQueue(CommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public PendingCommand queueCommand(String deviceId, CommandType commandType) {
        PendingCommand command = PendingCommand.create(deviceId, commandType);

        PendingCommand savedCommand = commandRepository.save(command);

        queuedCommandIdsByDevice
                .computeIfAbsent(deviceId, ignored -> new ConcurrentLinkedQueue<>())
                .add(savedCommand.id());

        return savedCommand;
    }

    public PendingCommand pollCommand(String deviceId) {
        Queue<String> queue = queuedCommandIdsByDevice.get(deviceId);

        if (queue == null) {
            return null;
        }

        while (!queue.isEmpty()) {
            String commandId = queue.poll();
            PendingCommand command = commandRepository.findById(commandId);

            if (command == null) {
                continue;
            }

            if (command.status() != CommandStatus.QUEUED) {
                continue;
            }

            PendingCommand deliveredCommand = command.markDelivered();
            return commandRepository.update(deliveredCommand);
        }

        return null;
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
