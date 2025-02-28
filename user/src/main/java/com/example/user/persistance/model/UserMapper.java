package com.example.user.persistance.model;


import com.example.user.core.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromEntity(UserEntity userEntity);

    UserEntity fromDomain(User user);

    List<User> fromEntities(List<UserEntity> requestAuthorityEntity);
}
