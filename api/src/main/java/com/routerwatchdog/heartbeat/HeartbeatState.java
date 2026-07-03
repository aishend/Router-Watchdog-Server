package com.routerwatchdog.heartbeat;

import com.routerwatchdog.heartbeat.dto.HeartbeatRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class HeartbeatState {
    private final ConcurrentMap<String, DeviceHeartbeat> devices = new ConcurrentHashMap<>();

    public void save(HeartbeatRequest request) {
        devices.put(request.deviceId(), new DeviceHeartbeat(request, Instant.now()));
    }

    public Collection<DeviceHeartbeat> getDevices() {
        return devices.values().stream()
                .sorted(Comparator.comparing(device -> device.request().deviceId()))
                .toList();
    }

    public record DeviceHeartbeat(
            HeartbeatRequest request,
            Instant lastReceivedAt
    ) {
    }
}
