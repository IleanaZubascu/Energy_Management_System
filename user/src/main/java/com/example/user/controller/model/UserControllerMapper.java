package com.example.user.controller.model;

import com.example.user.core.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserControllerMapper {

    public abstract List<GetUserResponse> toDTOs(List<User> users);
}
