package com.example.monitoring_communication.core.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnergyConsumption {

    private Long deviceId;

    private Long timestamp;

    private Double hourlyConsumption;
}
