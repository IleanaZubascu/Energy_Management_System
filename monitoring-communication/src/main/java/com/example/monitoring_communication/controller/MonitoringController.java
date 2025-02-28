package com.example.monitoring_communication.controller;

import com.example.monitoring_communication.controller.model.CreateEnergyConsumptionResponse;
import com.example.monitoring_communication.core.EnergyConsumptionService;
import com.example.monitoring_communication.core.RabbitService;
import com.example.monitoring_communication.core.model.EnergyConsumption;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/monitoring")
public class MonitoringController {

    private final EnergyConsumptionService energyConsumptionService;
    private final RabbitService rabbitService;

    public MonitoringController(EnergyConsumptionService energyConsumptionService, RabbitService rabbitService) {
        this.energyConsumptionService = energyConsumptionService;
        this.rabbitService = rabbitService;
    }

    @PostMapping
    public ResponseEntity<CreateEnergyConsumptionResponse> create(
            @RequestBody final EnergyConsumption createRequest) {
        final var energyConsumption = energyConsumptionService.save(createRequest);

        return ResponseEntity.ok(
                CreateEnergyConsumptionResponse.builder()
                        .id(energyConsumption.getId())
                        .deviceId(energyConsumption.getDeviceId())
                        .hourlyConsumption(energyConsumption.getHourlyConsumption())
                        .timestamp(energyConsumption.getTimestamp())
                        .build());
    }

    @PostMapping("/start")
    public String startSimulation(@RequestParam Long deviceId)
    {
        rabbitService.run(deviceId);
        return "Start simulation for device with id " + deviceId;
    }

    @PostMapping("/stop")
    public String stopSimulation(@RequestParam Long deviceId) {
        rabbitService.stop(deviceId);
        return "Stop simulation for device with id " + deviceId;
    }


}
