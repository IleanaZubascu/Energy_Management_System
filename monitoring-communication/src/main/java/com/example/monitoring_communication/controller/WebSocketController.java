package com.example.monitoring_communication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendAlert(Long deviceId, Double totalConsumption) {
        String alertMessage = "ALERT: Consumption exceeded for device with ID: " + deviceId
                + " ,total consumption: " + totalConsumption;
        Map<String, Object> alertMap = new HashMap<>();
        alertMap.put("deviceId", deviceId);
        alertMap.put("message", alertMessage);

        try{
            String jsonAlert = new ObjectMapper().writeValueAsString(alertMap);
            this.messagingTemplate.convertAndSend("/topic/alerts", jsonAlert);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

