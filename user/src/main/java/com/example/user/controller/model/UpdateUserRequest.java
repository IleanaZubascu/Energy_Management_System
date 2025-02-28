package com.example.user.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @NotNull
    private Long id;

    @NotNull
    private String name;
}
