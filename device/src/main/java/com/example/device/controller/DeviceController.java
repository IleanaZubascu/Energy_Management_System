package com.example.device.controller;


import com.example.device.controller.model.CreateDeviceRequest;
import com.example.device.controller.model.DeviceControllerMapper;
import com.example.device.controller.model.DeviceDTO;
import com.example.device.core.model.Device;
import com.example.device.core.ports.incoming.DeviceManagementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/device")
public class DeviceController {

    private final DeviceManagementService deviceManagementService;

    private final DeviceControllerMapper deviceControllerMapper;



    public DeviceController(DeviceManagementService deviceManagementService, DeviceControllerMapper deviceControllerMapper) {
        this.deviceManagementService = deviceManagementService;
        this.deviceControllerMapper = deviceControllerMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<DeviceDTO> createDevice(
            @Valid @RequestBody final CreateDeviceRequest createDeviceRequest) {
        final var device = deviceManagementService.save(Device.builder()
                .address(createDeviceRequest.getAddress())
                .description(createDeviceRequest.getDescription())
                .maxEnergy(createDeviceRequest.getMaxEnergy())
                .build());
        return ResponseEntity.ok(DeviceDTO.builder()
                .id(device.getId())
                .address(device.getAddress())
                .description(device.getDescription())
                .maxEnergy(device.getMaxEnergy())
                .userId(device.getUserId())
                .build());

    }

    @GetMapping("/devices/{userId}")
    public ResponseEntity<List<DeviceDTO>> getAllDevicesByUserId(@PathVariable Long userId) {

        final var devices = deviceManagementService.findDevicesByUserId(userId);
        return ResponseEntity.ok(
                deviceControllerMapper.toDTOs(devices)
        );
    }

    @PostMapping("/update")
    public ResponseEntity<DeviceDTO> updateDevice(@RequestBody @Valid DeviceDTO deviceDTO) {
        if (!deviceManagementService.checkUserExists(deviceDTO.getUserId())) {
            throw new IllegalStateException("User with ID " + deviceDTO.getUserId() + " does not exist.");
        }

        final var device = deviceManagementService.updateDevice(deviceControllerMapper.toModel(deviceDTO));
        return ResponseEntity.ok(deviceControllerMapper.toDTO(device));
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@NotNull final Long deviceId) {
        deviceManagementService.deleteDevice(deviceId);
    }

    @GetMapping("/devices")
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        List<DeviceDTO> deviceDTOs = deviceControllerMapper.toDTOs(deviceManagementService.findDevices());
        deviceDTOs.sort(Comparator.comparing(DeviceDTO::getId));

        return ResponseEntity.ok(deviceDTOs);
    }
}
