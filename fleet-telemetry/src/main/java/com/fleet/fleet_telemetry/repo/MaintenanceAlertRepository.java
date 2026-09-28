package com.fleet.fleet_telemetry.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fleet.fleet_telemetry.model.MaintenanceAlert;

public interface MaintenanceAlertRepository extends JpaRepository<MaintenanceAlert, Long> {
    List<MaintenanceAlert> findByVehicleId(Long vehicleId);
}