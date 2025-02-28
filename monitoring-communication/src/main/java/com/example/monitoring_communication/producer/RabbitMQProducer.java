package com.example.monitoring_communication.producer;

import com.example.monitoring_communication.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CSV_FILE = "./sensor.csv";
    private static final Map<Long, AtomicBoolean> deviceRunningStatus = new ConcurrentHashMap<>();
    private static final Map<Long, Thread> deviceSimulationThreads = new ConcurrentHashMap<>();

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessages(Long deviceId) {
        AtomicBoolean running = deviceRunningStatus.computeIfAbsent(deviceId, id -> new AtomicBoolean(true));
        running.set(true);

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null && running.get()) {
                double measurementValue = Double.parseDouble(line.trim());

                Map<String, Object> message = new HashMap<>();
                message.put("timestamp", Instant.now().toEpochMilli());
                message.put("device_id", deviceId.toString());
                message.put("measurement_value", measurementValue);

                String jsonMessage = objectMapper.writeValueAsString(message);

                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,
                        "monitoring.energy.data",
                        jsonMessage);

                System.out.println("Sent message: " + jsonMessage);

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(Long deviceId) {
        AtomicBoolean running = deviceRunningStatus.get(deviceId);
        if (running != null) {
            running.set(false);
            deviceSimulationThreads.remove(deviceId);
        }
    }

    public void run(Long deviceId) {
        Thread simulationThread = new Thread(() -> sendMessages(deviceId));
        deviceSimulationThreads.put(deviceId, simulationThread);
        simulationThread.start();
    }

}
