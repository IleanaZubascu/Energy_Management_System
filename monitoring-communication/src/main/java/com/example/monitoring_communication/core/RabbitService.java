package com.example.monitoring_communication.core;

import com.example.monitoring_communication.producer.RabbitMQProducer;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

    private final RabbitMQProducer rabbitMQProducer;

    public RabbitService(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    public void run(Long deviceId)
    {
        rabbitMQProducer.run(deviceId);
    }

    public void stop(Long deviceId)
    {
        rabbitMQProducer.stop(deviceId);
    }

}
