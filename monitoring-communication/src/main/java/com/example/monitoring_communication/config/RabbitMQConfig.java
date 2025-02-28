package com.example.monitoring_communication.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "device_monitoring_exchange";

    public static final String ENERGY_DATA_QUEUE = "energy_data_queue";
    public static final String DEVICE_UPDATE_QUEUE = "device_update_queue";
    public static final String DEVICE_RESPONSE_QUEUE = "device_to_monitoring_response_queue";
    public static final String DEVICE_REQUEST_QUEUE = "monitoring_to_device_request_queue";


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }


    @Bean
    public Queue deviceUpdateQueue() {
        return new Queue(DEVICE_UPDATE_QUEUE, true);
    }

    @Bean
    public Queue deviceRequestQueue() {
        return new Queue(DEVICE_REQUEST_QUEUE, true);
    }

    @Bean
    public Queue deviceResponseQueue() {
        return new Queue(DEVICE_RESPONSE_QUEUE, true);
    }

    @Bean
    public Queue energyDataQueue() {
        return new Queue(ENERGY_DATA_QUEUE, true);
    }

    @Bean
    public Binding bindEnergyDataQueue(Queue energyDataQueue, TopicExchange exchange) {
        return BindingBuilder.bind(energyDataQueue).to(exchange).with("monitoring.energy.data");
    }

    @Bean
    public Binding bindDeviceRequestQueue(Queue deviceRequestQueue, TopicExchange exchange) {
        return BindingBuilder.bind(deviceRequestQueue).to(exchange).with("monitoring.to.device.request");
    }

    @Bean
    public Binding bindDeviceResponseQueue(Queue deviceResponseQueue, TopicExchange exchange) {
        return BindingBuilder.bind(deviceResponseQueue).to(exchange).with("device.to.monitoring.response");
    }

    @Bean
    public Binding bindDeviceUpdateQueue(Queue deviceUpdateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(deviceUpdateQueue).to(exchange).with("device.update");
    }
}
