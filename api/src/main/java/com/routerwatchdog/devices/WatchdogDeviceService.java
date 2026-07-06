package com.routerwatchdog.devices;

import com.routerwatchdog.devices.dto.UpdateDeviceMetadataRequest;
import com.routerwatchdog.heartbeat.dto.HeartbeatRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class WatchdogDeviceService {

    private final WatchdogDeviceRepository watchdogDeviceRepository;

    public WatchdogDeviceService(WatchdogDeviceRepository watchdogDeviceRepository) {
        this.watchdogDeviceRepository = watchdogDeviceRepository;
    }

    public WatchdogDeviceEntity recordHeartbeat(HeartbeatRequest request, Instant receivedAt) {
        WatchdogDeviceEntity device = watchdogDeviceRepository
                .findById(request.deviceId())
                .orElseGet(() -> new WatchdogDeviceEntity(
                        request.deviceId(),
                        request.ip(),
                        request.gateway(),
                        request.failures(),
                        request.uptime(),
                        request.rssi(),
                        request.freeHeap(),
                        request.firmwareVersion(),
                        request.deviceId(),
                        null,
                        null,
                        true,
                        receivedAt,
                        receivedAt
                ));

        device.updateFromHeartbeat(
                request.ip(),
                request.gateway(),
                request.failures(),
                request.uptime(),
                request.rssi(),
                request.freeHeap(),
                request.firmwareVersion(),
                receivedAt
        );

        return watchdogDeviceRepository.save(device);
    }

    public List<WatchdogDeviceEntity> getDevicesByArrivalOrder() {
        return watchdogDeviceRepository.findAllByOrderByFirstSeenAtAsc();
    }

    public WatchdogDeviceEntity updateMetadata(
            String deviceId,
            UpdateDeviceMetadataRequest request
    ) {
        WatchdogDeviceEntity device = watchdogDeviceRepository
                .findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + deviceId));

        device.updateMetadata(
                request.displayName(),
                request.location(),
                request.notes(),
                request.enabled()
        );

        return watchdogDeviceRepository.save(device);
    }
}