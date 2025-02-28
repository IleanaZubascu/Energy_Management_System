package com.example.device.controller.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceDTO {

    private Long id;
    private String description;
    private String address;
    private Long maxEnergy;
    private Long userId;
}
