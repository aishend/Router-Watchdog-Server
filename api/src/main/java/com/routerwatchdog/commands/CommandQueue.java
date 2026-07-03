package com.routerwatchdog.commands;

import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Component
public class CommandQueue {
    private final ConcurrentMap<String, Queue<CommandType>> commandsByDevice = new ConcurrentHashMap<>();

    public void queueCommand(String deviceId, CommandType command) {
        if (command == CommandType.NONE) {
            return;
        }

        commandsByDevice
                .computeIfAbsent(deviceId, ignored -> new ConcurrentLinkedQueue<>())
                .add(command);
    }

    public CommandType pollCommand(String deviceId) {
        Queue<CommandType> commands = commandsByDevice.get(deviceId);

        if (commands == null) {
            return CommandType.NONE;
        }

        CommandType command = commands.poll();

        if (command == null) {
            return CommandType.NONE;
        }

        return command;
    }
}