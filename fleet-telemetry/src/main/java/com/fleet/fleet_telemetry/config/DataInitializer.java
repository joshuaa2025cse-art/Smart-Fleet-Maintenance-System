package com.fleet.fleet_telemetry.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fleet.fleet_telemetry.model.MaintenanceAlert;
import com.fleet.fleet_telemetry.model.TireTelemetry;
import com.fleet.fleet_telemetry.model.User;
import com.fleet.fleet_telemetry.model.Vehicle;
import com.fleet.fleet_telemetry.service.MaintenanceAlertService;
import com.fleet.fleet_telemetry.service.TelemetryService;
import com.fleet.fleet_telemetry.service.UserService;
import com.fleet.fleet_telemetry.service.VehicleService;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initSampleData(
            VehicleService vehicleService,
            TelemetryService telemetryService,
            MaintenanceAlertService alertService,
            UserService userService) {
        return args -> {
            log.info("=================================================================");
            log.info("🚀 INITIALIZING FLEET TELEMETRY SERVICE");
            log.info("=================================================================");

            // Seed Default User
            if (userService.count() == 0) {
                User demoUser = new User();
                demoUser.setUsername("admin");
                demoUser.setEmail("admin@fleet.com");
                demoUser.setPassword("admin123");
                demoUser.setFullName("Fleet Administrator");
                demoUser.setRole("FLEET_MANAGER");
                userService.save(demoUser);
                log.info("👤 Seeded Default User: admin / admin123 (admin@fleet.com)");
            }

            if (vehicleService.getAllVehicles().isEmpty()) {
                // 1. Seed Vehicles
                Vehicle v1 = new Vehicle();
                v1.setLicensePlate("KA-01-AB-1234");
                v1.setModel("Volvo FH16 Heavy Truck");
                v1.setStatus("ACTIVE");
                v1 = vehicleService.saveVehicle(v1);

                Vehicle v2 = new Vehicle();
                v2.setLicensePlate("MH-02-CD-5678");
                v2.setModel("Scania R-Series Hauler");
                v2.setStatus("ACTIVE");
                v2 = vehicleService.saveVehicle(v2);

                log.info("✅ Seeded Vehicles: [ID: {}, Plate: {}], [ID: {}, Plate: {}]",
                        v1.getId(), v1.getLicensePlate(), v2.getId(), v2.getLicensePlate());

                // 2. Ingest Normal Telemetry for Vehicle 1
                TireTelemetry t1 = new TireTelemetry();
                t1.setVehicleId(v1.getId());
                t1.setTirePressure(34.5);
                t1.setTireTemperature(65.0);
                t1.setTreadDepth(7.2);
                t1 = telemetryService.processTelemetry(t1);
                log.info("📊 Telemetry Ingested for Vehicle {}: Status = {}", v1.getId(), t1.getHealthStatus());

                // 3. Ingest Critical Telemetry for Vehicle 2 (Low pressure: 27.5 PSI, high temp: 88.0 C)
                TireTelemetry t2 = new TireTelemetry();
                t2.setVehicleId(v2.getId());
                t2.setTirePressure(27.5);
                t2.setTireTemperature(88.0);
                t2.setTreadDepth(1.8);
                t2 = telemetryService.processTelemetry(t2);
                log.info("⚠️ Critical Telemetry Ingested for Vehicle {}: Status = {}", v2.getId(), t2.getHealthStatus());

                // 4. Verify Generated Alerts
                List<MaintenanceAlert> alerts = alertService.getAllAlerts();
                log.info("🚨 Total Active Maintenance Alerts Generated: {}", alerts.size());
                for (MaintenanceAlert alert : alerts) {
                    log.info("   -> [Alert ID: {}] Vehicle {}: {} (Severity: {})",
                            alert.getId(), alert.getVehicleId(), alert.getAlertMessage(), alert.getSeverity());
                }
            } else {
                log.info("ℹ️ Existing vehicles already present in database (count: {}), skipping seed data.",
                        vehicleService.getAllVehicles().size());
            }

            log.info("=================================================================");
            log.info("🌟 FLEET TELEMETRY SERVICE IS FULLY OPERATIONAL AND READY!");
            log.info("   Endpoints available on http://localhost:8080:");
            log.info("   - GET  /api/vehicles");
            log.info("   - GET  /api/telemetry/vehicle/1");
            log.info("   - GET  /api/maintenance-alerts");
            log.info("   - POST /api/telemetry");
            log.info("   - H2 Console: http://localhost:8080/h2-console");
            log.info("=================================================================");
        };
    }
}
