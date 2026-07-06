package com.routerwatchdog.devices;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
}