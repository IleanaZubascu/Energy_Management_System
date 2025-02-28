package com.example.user.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsResponse {
    private Long id;
    private String name;
    private String authority;
}
