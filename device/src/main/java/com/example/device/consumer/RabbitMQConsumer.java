package com.example.device.consumer;

import aj.org.objectweb.asm.TypeReference;
import com.example.device.config.RabbitMQConfig;
import com.example.device.core.model.Device;
import com.example.device.core.ports.incoming.DeviceManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitMQConsumer {

    private final ObjectMapper mapper = new ObjectMapper();
    private final DeviceManagementService deviceManagementService;
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQConsumer(DeviceManagementService deviceManagementService, RabbitTemplate rabbitTemplate) {
        this.deviceManagementService = deviceManagementService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.DEVICE_REQUEST_QUEUE)
    public void receive(String message) {
        try {
            System.out.println("Received message: " + message);

            Map<String, Object> data = mapper.readValue(message, Map.class);
            Long deviceId = Long.valueOf(data.get("deviceId").toString());

                Device device = deviceManagementService.findById(deviceId);
                Double maxEnergy = device.getMaxEnergy().doubleValue();

                Map<String, Object> response = new HashMap<>();
                response.put("deviceId", deviceId);
                response.put("maxEnergy", maxEnergy);

                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE,
                        "device.to.monitoring.response",
                        mapper.writeValueAsString(response));

                System.out.println("Sent maxEnergy: " +maxEnergy + " response for deviceId: " + deviceId);
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

}
