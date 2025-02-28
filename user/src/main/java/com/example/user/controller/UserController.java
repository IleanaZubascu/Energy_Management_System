package com.example.user.controller;

import com.example.user.controller.model.GetUserResponse;
import com.example.user.controller.model.UpdateUserRequest;
import com.example.user.controller.model.UserControllerMapper;
import com.example.user.controller.model.UserDTO;
import com.example.user.core.model.User;
import com.example.user.core.ports.incoming.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {


    private final UserService userManagementService;
    private final UserControllerMapper userControllerMapper;

    public UserController(UserService userManagementService, UserControllerMapper userControllerMapper) {
        this.userManagementService = userManagementService;
        this.userControllerMapper = userControllerMapper;
    }


    @GetMapping("/get/{userId}")
    public ResponseEntity<UserDTO> getUser(@Valid @RequestParam Long userId) {

        final var user = userManagementService.findById(userId).orElseThrow(
                () -> new IllegalStateException("User with id " + userId + " not found.")
        );

        return ResponseEntity.ok(UserDTO.builder()
                .name(user.getName())
                .password(user.getPassword())
                .build());
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@NotNull final Long userId) {
        userManagementService.delete(userId);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {

        userManagementService.update(
                User.builder()
                        .id(updateUserRequest.getId())
                        .name(updateUserRequest.getName())
                        .build()
        );
    }

    @GetMapping("/users")
    public ResponseEntity<List<GetUserResponse>> getUsers() {
        return ResponseEntity.ok(
                userControllerMapper.toDTOs(userManagementService.findAll())
        );
    }

    @GetMapping("/exists/{userId}")
    public ResponseEntity<Boolean> existsUser(@PathVariable Long userId) {
        final var existsUser = userManagementService.findById(userId).isPresent();
        return ResponseEntity.ok(existsUser);
    }



}

