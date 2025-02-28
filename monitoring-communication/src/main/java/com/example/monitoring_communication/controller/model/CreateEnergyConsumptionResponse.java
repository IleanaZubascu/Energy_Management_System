package com.example.monitoring_communication.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEnergyConsumptionResponse {
    private Long id;
    private Long deviceId;

    private Long timestamp;

    private Double hourlyConsumption;
}
