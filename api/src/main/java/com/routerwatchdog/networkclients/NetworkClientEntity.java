package com.routerwatchdog.networkclients;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "network_clients")
public class NetworkClientEntity {

    @Id
    private String id;

    private String watchdogDeviceId;
    private String ip;
    private String mac;
    private String hostname;
    private String vendor;
    private Instant firstSeenAt;
    private Instant lastSeenAt;

    protected NetworkClientEntity() {
    }

    public NetworkClientEntity(
            String id,
            String watchdogDeviceId,
            String ip,
            String mac,
            String hostname,
            String vendor,
            Instant firstSeenAt,
            Instant lastSeenAt
    ) {
        this.id = id;
        this.watchdogDeviceId = watchdogDeviceId;
        this.ip = ip;
        this.mac = mac;
        this.hostname = hostname;
        this.vendor = vendor;
        this.firstSeenAt = firstSeenAt;
        this.lastSeenAt = lastSeenAt;
    }

    public String getId() {
        return id;
    }

    public String getWatchdogDeviceId() {
        return watchdogDeviceId;
    }

    public String getIp() {
        return ip;
    }

    public String getMac() {
        return mac;
    }

    public String getHostname() {
        return hostname;
    }

    public String getVendor() {
        return vendor;
    }

    public Instant getFirstSeenAt() {
        return firstSeenAt;
    }

    public Instant getLastSeenAt() {
        return lastSeenAt;
    }

    public void updateSeen(
            String ip,
            String hostname,
            String vendor,
            Instant seenAt
    ) {
        this.ip = ip;
        this.hostname = hostname;
        this.vendor = vendor;
        this.lastSeenAt = seenAt;
    }
}
