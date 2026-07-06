package com.routerwatchdog.networkclients;

import com.routerwatchdog.networkclients.dto.NetworkClientReportRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/network-clients")
public class NetworkClientController {

    private final NetworkClientService networkClientService;

    public NetworkClientController(NetworkClientService networkClientService) {
        this.networkClientService = networkClientService;
    }

    @GetMapping
    public ResponseEntity<?> getRecentClients() {
        return ResponseEntity.ok(
                Map.of(
                        "clients", networkClientService.getRecentClients()
                )
        );
    }

    @GetMapping("/{watchdogDeviceId}")
    public ResponseEntity<?> getRecentClientsByWatchdogDevice(
            @PathVariable String watchdogDeviceId
    ) {
        return ResponseEntity.ok(
                Map.of(
                        "clients", networkClientService.getRecentClientsByWatchdogDevice(
                                watchdogDeviceId
                        )
                )
        );
    }

    @PostMapping("/report")
    public ResponseEntity<?> reportClients(
            @Valid @RequestBody NetworkClientReportRequest request
    ) {
        return ResponseEntity.ok(
                Map.of(
                        "clients", networkClientService.recordReport(request)
                )
        );
    }
}
