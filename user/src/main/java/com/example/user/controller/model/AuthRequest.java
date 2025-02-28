package com.example.user.controller.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequest {

    @NotNull
    private String name;

    @NotNull
    private String password;
}
