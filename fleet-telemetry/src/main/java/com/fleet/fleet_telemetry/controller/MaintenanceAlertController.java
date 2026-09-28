package com.fleet.fleet_telemetry.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fleet.fleet_telemetry.model.MaintenanceAlert;
import com.fleet.fleet_telemetry.service.MaintenanceAlertService;

@RestController
@RequestMapping("/api/maintenance-alerts")
public class MaintenanceAlertController {

    private final MaintenanceAlertService alertService;

    public MaintenanceAlertController(MaintenanceAlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceAlert>> getAllAlerts() {
        return ResponseEntity.ok(alertService.getAllAlerts());
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<MaintenanceAlert>> getAlertsByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(alertService.getAlertsByVehicle(vehicleId));
    }
}
