package com.example.device.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDeviceRequest {

    @NotNull
    private String description;
    @NotNull
    private String address;
    @NotNull
    private Long maxEnergy;
}
