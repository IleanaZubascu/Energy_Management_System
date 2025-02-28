package com.example.monitoring_communication.consumer;

import com.example.monitoring_communication.config.RabbitMQConfig;
import com.example.monitoring_communication.controller.WebSocketController;
import com.example.monitoring_communication.core.EnergyConsumptionService;
import com.example.monitoring_communication.core.model.EnergyConsumption;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class RabbitMQConsumer {

    private final EnergyConsumptionService energyConsumptionService;
    private final WebSocketController webSocketController;
    private Map<Long, List<Double>> measurements = new HashMap<>();
    private final Map<Long, Double> maxEnergyMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RabbitTemplate rabbitTemplate;
    private final Map<Long, AtomicBoolean> deviceReadyStatus = new ConcurrentHashMap<>();


    public RabbitMQConsumer(EnergyConsumptionService energyConsumptionService, WebSocketController webSocketController, RabbitTemplate rabbitTemplate) {
        this.energyConsumptionService = energyConsumptionService;
        this.webSocketController = webSocketController;
        this.rabbitTemplate = rabbitTemplate;
    }


    @RabbitListener(queues = RabbitMQConfig.ENERGY_DATA_QUEUE)
    public void consumeMessage(String message) throws JsonProcessingException {
        Map<String, Object> messageMap = objectMapper.readValue(message, Map.class);
        System.out.println("Received message: " + messageMap);

        Double measurementValue = (Double) messageMap.get("measurement_value");
        Long deviceId = Long.parseLong((String) messageMap.get("device_id"));
        Long timestamp = (Long) messageMap.get("timestamp");

        measurements.computeIfAbsent(deviceId, k -> new ArrayList<>()).add(measurementValue);

        if (measurements.get(deviceId).size() >= 6) {
            System.out.println("BEGIN");
            Double totalMeasurement = measurements.get(deviceId).stream().mapToDouble(Double::doubleValue).sum();
            measurements.get(deviceId).clear();

            if (energyConsumptionService.existsByDeviceId(deviceId)) {
                energyConsumptionService.updateHourlyConsumptionAndTimestampByDeviceId(deviceId, totalMeasurement, timestamp);
            } else {
                energyConsumptionService.save(
                        EnergyConsumption.builder()
                                .deviceId(deviceId)
                                .timestamp(timestamp)
                                .hourlyConsumption(totalMeasurement)
                                .build()
                );
            }

            if (!maxEnergyMap.containsKey(deviceId)) {
                Map<String, Object> request = new HashMap<>();
                request.put("deviceId", deviceId);
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,
                        "monitoring.to.device.request",
                        objectMapper.writeValueAsString(request));
                System.out.println("Sent request for maxEnergy of deviceId: " + deviceId);

                waitForMaxEnergy(deviceId);
            }

            Double hourlyConsumption = energyConsumptionService.getHourlyConsumptionByDeviceId(deviceId);
            Double maxEnergy = maxEnergyMap.get(deviceId);

            System.out.println("Hourly consumption: " + hourlyConsumption);
            System.out.println("Max energy: " + maxEnergy);

            if (hourlyConsumption > maxEnergy) {
                String alertMessage = "ALERT: Consumption exceeded for device with ID: " + deviceId;
                System.out.println(alertMessage);
                webSocketController.sendAlert(deviceId, hourlyConsumption);
            }
        }
    }

    @RabbitListener(queues = RabbitMQConfig.DEVICE_UPDATE_QUEUE)
    public void consumeDeviceUpdate(String message) {
        try {
            System.out.println("DEVICE update: " + message);

            Map<String, Object> messageMap = objectMapper.readValue(message, Map.class);

            Long deviceId = Long.valueOf(messageMap.get("deviceId").toString());
            Double maxEnergy = Double.valueOf(messageMap.get("maxEnergy").toString());

            maxEnergyMap.put(deviceId, maxEnergy);
            System.out.println("Updated maxEnergy for deviceId " + deviceId + " maxEnergy: " + maxEnergyMap.get(deviceId));
        } catch (Exception e) {
            System.out.println("Error while processing the device update: " + e.getMessage());
        }
    }
    @RabbitListener(queues = RabbitMQConfig.DEVICE_RESPONSE_QUEUE)
    public synchronized void consumeDeviceResponse(String message) {
        try {
            System.out.println("From DEVICE response: " + message);

            Map<String, Object> messageMap = objectMapper.readValue(message, Map.class);

            Long deviceId = Long.valueOf(messageMap.get("deviceId").toString());
            Double maxEnergy = Double.valueOf(messageMap.get("maxEnergy").toString());

            maxEnergyMap.put(deviceId, maxEnergy);
            deviceReadyStatus.put(deviceId, new AtomicBoolean(true));

            notifyAll();
        } catch (Exception e) {
            System.out.println("Error while processing the device response: " + e.getMessage());
        }
    }
    private synchronized void waitForMaxEnergy(Long deviceId) {
        while (!maxEnergyMap.containsKey(deviceId)) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
