package com.fleet.fleet_telemetry.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fleet.fleet_telemetry.model.TireTelemetry;

public interface TireTelemetryRepository extends JpaRepository<TireTelemetry, Long> {
    List<TireTelemetry> findByVehicleId(Long vehicleId);
}