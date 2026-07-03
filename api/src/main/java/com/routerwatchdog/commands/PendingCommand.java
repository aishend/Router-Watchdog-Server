package com.routerwatchdog.commands;

import java.time.Instant;
import java.util.UUID;

public record PendingCommand(
        String id,
        CommandType type,
        CommandStatus status,
        Instant createdAt,
        Instant deliveredAt,
        Instant completedAt) {
    public static PendingCommand create(CommandType type) {
        return new PendingCommand(
                UUID.randomUUID().toString(),
                type,
                CommandStatus.QUEUED,
                Instant.now(),
                null,
                null);
    }

    public PendingCommand markDelivered() {
        return new PendingCommand(
                id,
                type,
                CommandStatus.DELIVERED,
                createdAt,
                Instant.now(),
                completedAt);
    }

    public PendingCommand markCompleted() {
        return new PendingCommand(
                id,
                type,
                CommandStatus.COMPLETED,
                createdAt,
                deliveredAt,
                Instant.now());
    }

    public PendingCommand markFailed() {
        return new PendingCommand(
                id,
                type,
                CommandStatus.FAILED,
                createdAt,
                deliveredAt,
                Instant.now());
    }
}