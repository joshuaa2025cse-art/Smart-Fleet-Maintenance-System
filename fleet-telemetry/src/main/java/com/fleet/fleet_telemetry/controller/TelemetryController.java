package com.fleet.fleet_telemetry.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fleet.fleet_telemetry.model.TireTelemetry;
import com.fleet.fleet_telemetry.service.TelemetryService;

@RestController
@RequestMapping("/api/telemetry")public class TelemetryController {

    private final TelemetryService telemetryService;

    public TelemetryController(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    @PostMapping
    public ResponseEntity<TireTelemetry> ingestTelemetry(@RequestBody TireTelemetry telemetry) {
        TireTelemetry savedTelemetry = telemetryService.processTelemetry(telemetry);
        return ResponseEntity.ok(savedTelemetry);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<TireTelemetry>> getVehicleTelemetry(@PathVariable Long vehicleId) {
        List<TireTelemetry> history = telemetryService.getTelemetryByVehicle(vehicleId);
        return ResponseEntity.ok(history);
    }
}