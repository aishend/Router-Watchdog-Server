package com.routerwatchdog.commands;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "commands")
public class CommandEntity {

    @Id
    private String id;

    private String deviceId;

    @Enumerated(EnumType.STRING)
    private CommandType type;

    @Enumerated(EnumType.STRING)
    private CommandStatus status;

    private Instant createdAt;
    private Instant deliveredAt;
    private Instant completedAt;

    protected CommandEntity() {
    }

    public CommandEntity(
            String id,
            String deviceId,
            CommandType type,
            CommandStatus status,
            Instant createdAt,
            Instant deliveredAt,
            Instant completedAt
    ) {
        this.id = id;
        this.deviceId = deviceId;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.deliveredAt = deliveredAt;
        this.completedAt = completedAt;
    }

    public String getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public CommandType getType() {
        return type;
    }

    public CommandStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getDeliveredAt() {
        return deliveredAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }
}
