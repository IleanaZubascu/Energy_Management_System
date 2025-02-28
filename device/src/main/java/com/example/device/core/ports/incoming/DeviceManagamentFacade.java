package com.example.device.core.ports.incoming;


import com.example.device.core.model.Device;
import com.example.device.core.ports.outgoing.DeviceRepository;
import com.example.device.producer.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DeviceManagamentFacade implements DeviceManagementService {

    private final DeviceRepository deviceRepository;

    private final RestTemplate restTemplate;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

   // @Value("http://user:8082")
    //@Value("http://localhost:8082")
    @Value("http://user.localhost")
    private String userServiceUrl;

    public DeviceManagamentFacade(DeviceRepository deviceRepository, RestTemplate restTemplate) {
        this.deviceRepository = deviceRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    @Override
    public List<Device> findDevicesByUserId(Long userId) {
        return deviceRepository.findByUserId(userId);
    }

    @Override
    public Device updateDevice(Device device) {

        Device updatedDevice = deviceRepository.updateDevice(device);
        Double maxEnergy = device.getMaxEnergy().doubleValue();
        String jsonMessage = String.format(
                "{\"deviceId\":\"%s\",\"maxEnergy\":%s}",
                updatedDevice.getId(),
                maxEnergy
        );
        rabbitMQProducer.sendDeviceUpdate(jsonMessage);
        return updatedDevice;
    }

    @Override
    public void deleteDevice(Long deviceId) {
        deviceRepository.delete(deviceId);

    }

    @Override
    public List<Device> findDevices() {
        return deviceRepository.findAll();
    }

    public boolean checkUserExists(Long userId) {
        String url = userServiceUrl + "/api/exists/" + userId;
        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
        return response.getBody() != null && response.getBody();
    }

    @Override
    public Device findById(Long id) {
        return deviceRepository.findById(id);
    }
}
