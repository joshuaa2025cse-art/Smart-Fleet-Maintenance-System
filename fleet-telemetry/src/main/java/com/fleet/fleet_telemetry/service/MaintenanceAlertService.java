package com.fleet.fleet_telemetry.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fleet.fleet_telemetry.model.MaintenanceAlert;
import com.fleet.fleet_telemetry.repo.MaintenanceAlertRepository;

@Service
public class MaintenanceAlertService {

    private final MaintenanceAlertRepository alertRepository;

    public MaintenanceAlertService(MaintenanceAlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<MaintenanceAlert> getAllAlerts() {
        return alertRepository.findAll();
    }

    public List<MaintenanceAlert> getAlertsByVehicle(Long vehicleId) {
        return alertRepository.findByVehicleId(vehicleId);
    }
}
