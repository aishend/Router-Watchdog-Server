package com.leandro.routerwatchdog.heartbeat;

import com.leandro.routerwatchdog.heartbeat.dto.HeartbeatRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class HeartbeatState {

    private HeartbeatRequest lastRequest;
    private Instant lastReceivedAt;

    public void save(HeartbeatRequest request) {
        this.lastRequest = request;
        this.lastReceivedAt = Instant.now();
    }

    public HeartbeatRequest getLastRequest() {
        return lastRequest;
    }

    public Instant getLastReceivedAt() {
        return lastReceivedAt;
    }
}