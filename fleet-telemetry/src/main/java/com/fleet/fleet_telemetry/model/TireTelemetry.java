package com.fleet.fleet_telemetry.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tire_telemetry")
@Data
public class TireTelemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long vehicleId;
    private double tirePressure;    // in PSI
    private double tireTemperature; // in Celsius
    private double treadDepth;      // in mm
    private LocalDateTime timestamp;
    private String healthStatus;    // NORMAL, WARNING, CRITICAL

    public TireTelemetry() {
    }

    public TireTelemetry(Long id, Long vehicleId, double tirePressure, double tireTemperature, double treadDepth, LocalDateTime timestamp, String healthStatus) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.tirePressure = tirePressure;
        this.tireTemperature = tireTemperature;
        this.treadDepth = treadDepth;
        this.timestamp = timestamp;
        this.healthStatus = healthStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public double getTirePressure() {
        return tirePressure;
    }

    public void setTirePressure(double tirePressure) {
        this.tirePressure = tirePressure;
    }

    public double getTireTemperature() {
        return tireTemperature;
    }

    public void setTireTemperature(double tireTemperature) {
        this.tireTemperature = tireTemperature;
    }

    public double getTreadDepth() {
        return treadDepth;
    }

    public void setTreadDepth(double treadDepth) {
        this.treadDepth = treadDepth;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }
}