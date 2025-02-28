package com.example.device.core.model;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Device {

    private Long id;
    private String description;
    private String address;
    private Long maxEnergy;
    private Long userId;
}
