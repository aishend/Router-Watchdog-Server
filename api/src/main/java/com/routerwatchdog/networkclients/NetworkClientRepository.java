package com.routerwatchdog.networkclients;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NetworkClientRepository extends JpaRepository<NetworkClientEntity, String> {

    Optional<NetworkClientEntity> findByWatchdogDeviceIdAndMac(
            String watchdogDeviceId,
            String mac
    );

    List<NetworkClientEntity> findTop20ByOrderByLastSeenAtDesc();

    List<NetworkClientEntity> findTop20ByWatchdogDeviceIdOrderByLastSeenAtDesc(
            String watchdogDeviceId
    );
}
