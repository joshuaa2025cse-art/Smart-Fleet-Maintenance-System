package com.fleet.fleet_telemetry.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fleet.fleet_telemetry.model.MaintenanceAlert;
import com.fleet.fleet_telemetry.model.TireTelemetry;
import com.fleet.fleet_telemetry.repo.MaintenanceAlertRepository;
import com.fleet.fleet_telemetry.repo.TireTelemetryRepository;

@Service
public class TelemetryService {

    private final TireTelemetryRepository telemetryRepository;
    private final MaintenanceAlertRepository alertRepository;

    public TelemetryService(TireTelemetryRepository telemetryRepository,
            MaintenanceAlertRepository alertRepository) {
        this.telemetryRepository = telemetryRepository;
        this.alertRepository = alertRepository;
    }

    public TireTelemetry processTelemetry(TireTelemetry telemetry) {
        telemetry.setTimestamp(LocalDateTime.now());
        
        // Predictive logic rules for tires
        if (telemetry.getTirePressure() < 30.0 || telemetry.getTireTemperature() > 85.0 || telemetry.getTreadDepth() < 2.0) {
            telemetry.setHealthStatus("CRITICAL - MAINTENANCE REQUIRED");
        } else if (telemetry.getTirePressure() < 33.0 || telemetry.getTireTemperature() > 75.0) {
            telemetry.setHealthStatus("WARNING");
        } else {
            telemetry.setHealthStatus("NORMAL");
        }

        TireTelemetry savedTelemetry = telemetryRepository.save(telemetry);

        if ("CRITICAL - MAINTENANCE REQUIRED".equals(savedTelemetry.getHealthStatus())) {
            MaintenanceAlert alert = new MaintenanceAlert();
            alert.setVehicleId(savedTelemetry.getVehicleId());
            alert.setAlertMessage("Critical tire telemetry requires immediate maintenance");
            alert.setSeverity("CRITICAL");
            alert.setTimestamp(savedTelemetry.getTimestamp());
            alertRepository.save(alert);
        }

        return savedTelemetry;
    }

    public List<TireTelemetry> getTelemetryByVehicle(Long vehicleId) {
        return telemetryRepository.findByVehicleId(vehicleId);
    }
}