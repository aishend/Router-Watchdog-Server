package com.routerwatchdog.devices;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchdogDeviceRepository extends JpaRepository<WatchdogDeviceEntity, String> {

    List<WatchdogDeviceEntity> findAllByOrderByFirstSeenAtAsc();
}