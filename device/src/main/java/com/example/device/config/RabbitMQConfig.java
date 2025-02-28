package com.example.device.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "device_monitoring_exchange";
    public static final String DEVICE_RESPONSE_QUEUE = "device_response_queue";
    public static final String DEVICE_REQUEST_QUEUE = "device_request_queue";

    @Bean
    public TopicExchange deviceExchange() {
        return new TopicExchange(EXCHANGE);
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
    public Binding bindDeviceRequestQueue(Queue deviceRequestQueue, TopicExchange exchange) {
        return BindingBuilder.bind(deviceRequestQueue).to(exchange).with("monitoring.to.device.request");
    }

    @Bean
    public Binding bindDeviceResponseQueue(Queue deviceResponseQueue, TopicExchange exchange) {
        return BindingBuilder.bind(deviceResponseQueue).to(exchange).with("device.to.monitoring.response");
    }

}