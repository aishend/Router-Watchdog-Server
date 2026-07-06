package com.routerwatchdog.networkclients;

import com.routerwatchdog.networkclients.dto.NetworkClientItemRequest;
import com.routerwatchdog.networkclients.dto.NetworkClientReportRequest;
import com.routerwatchdog.networkclients.dto.NetworkClientResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class NetworkClientService {

    private final NetworkClientRepository networkClientRepository;

    public NetworkClientService(NetworkClientRepository networkClientRepository) {
        this.networkClientRepository = networkClientRepository;
    }

    public List<NetworkClientResponse> recordReport(NetworkClientReportRequest request) {
        Instant now = Instant.now();

        return request.clients().stream()
                .map(client -> recordClient(request.watchdogDeviceId(), client, now))
                .map(this::toResponse)
                .toList();
    }

    public List<NetworkClientResponse> getRecentClients() {
        return networkClientRepository.findTop20ByOrderByLastSeenAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<NetworkClientResponse> getRecentClientsByWatchdogDevice(String watchdogDeviceId) {
        return networkClientRepository
                .findTop20ByWatchdogDeviceIdOrderByLastSeenAtDesc(watchdogDeviceId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private NetworkClientEntity recordClient(
            String watchdogDeviceId,
            NetworkClientItemRequest client,
            Instant seenAt
    ) {
        NetworkClientEntity entity = networkClientRepository
                .findByWatchdogDeviceIdAndMac(watchdogDeviceId, client.mac())
                .orElseGet(() -> new NetworkClientEntity(
                        watchdogDeviceId + ":" + client.mac(),
                        watchdogDeviceId,
                        client.ip(),
                        client.mac(),
                        client.hostname(),
                        client.vendor(),
                        seenAt,
                        seenAt
                ));

        entity.updateSeen(
                client.ip(),
                client.hostname(),
                client.vendor(),
                seenAt
        );

        return networkClientRepository.save(entity);
    }

    private NetworkClientResponse toResponse(NetworkClientEntity entity) {
        return new NetworkClientResponse(
                entity.getId(),
                entity.getWatchdogDeviceId(),
                entity.getIp(),
                entity.getMac(),
                entity.getHostname(),
                entity.getVendor(),
                entity.getFirstSeenAt(),
                entity.getLastSeenAt()
        );
    }
}
