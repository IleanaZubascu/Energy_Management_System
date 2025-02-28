package com.example.monitoring_communication;

import com.example.monitoring_communication.producer.RabbitMQProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonitoringCommunicationApplication{


    public static void main(String[] args) {
        SpringApplication.run(MonitoringCommunicationApplication.class, args);
    }

}
