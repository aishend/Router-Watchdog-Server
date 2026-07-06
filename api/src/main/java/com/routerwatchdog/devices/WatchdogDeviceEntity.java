package com.routerwatchdog.devices;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "watchdog_devices")
public class WatchdogDeviceEntity {

    @Id
    private String deviceId;

    private String ip;
    private String gateway;
    private int failures;
    private long uptime;
    private Integer rssi;
    private long freeHeap;
    private String firmwareVersion;

    private String displayName;
    private String location;
    private String notes;
    private boolean enabled = true;

    private Instant firstSeenAt;
    private Instant lastReceivedAt;

    protected WatchdogDeviceEntity() {
    }

    public WatchdogDeviceEntity(
            String deviceId,
            String ip,
            String gateway,
            int failures,
            long uptime,
            Integer rssi,
            long freeHeap,
            String firmwareVersion,
            String displayName,
            String location,
            String notes,
            boolean enabled,
            Instant firstSeenAt,
            Instant lastReceivedAt
    ) {
        this.deviceId = deviceId;
        this.ip = ip;
        this.gateway = gateway;
        this.failures = failures;
        this.uptime = uptime;
        this.rssi = rssi;
        this.freeHeap = freeHeap;
        this.firmwareVersion = firmwareVersion;
        this.displayName = displayName;
        this.location = location;
        this.notes = notes;
        this.enabled = enabled;
        this.firstSeenAt = firstSeenAt;
        this.lastReceivedAt = lastReceivedAt;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getIp() {
        return ip;
    }

    public String getGateway() {
        return gateway;
    }

    public int getFailures() {
        return failures;
    }

    public long getUptime() {
        return uptime;
    }

    public Integer getRssi() {
        return rssi;
    }

    public long getFreeHeap() {
        return freeHeap;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Instant getFirstSeenAt() {
        return firstSeenAt;
    }

    public Instant getLastReceivedAt() {
        return lastReceivedAt;
    }

    public void updateFromHeartbeat(
            String ip,
            String gateway,
            int failures,
            long uptime,
            Integer rssi,
            long freeHeap,
            String firmwareVersion,
            Instant receivedAt
    ) {
        this.ip = ip;
        this.gateway = gateway;
        this.failures = failures;
        this.uptime = uptime;
        this.rssi = rssi;
        this.freeHeap = freeHeap;
        this.firmwareVersion = firmwareVersion;
        this.lastReceivedAt = receivedAt;
    }

    public void updateMetadata(
            String displayName,
            String location,
            String notes,
            boolean enabled
    ) {
        this.displayName = displayName;
        this.location = location;
        this.notes = notes;
        this.enabled = enabled;
    }
}