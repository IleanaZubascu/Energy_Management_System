package com.example.device.producer;

import com.example.device.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendDeviceUpdate(String message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                "device.update",
                message);
        System.out.println("UPDATE DEVICE: " + message);
    }
}

