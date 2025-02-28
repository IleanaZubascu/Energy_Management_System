package com.example.user.controller;


import com.example.user.config.JwtUtil;
import com.example.user.config.SecurityUtils;
import com.example.user.controller.model.AuthRequest;
import com.example.user.controller.model.UserDetailsResponse;
import com.example.user.core.model.User;
import com.example.user.core.ports.incoming.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody AuthRequest user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(User.builder()
                .name(user.getName())
                .password(user.getPassword())
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));

        User user = userService.findByName(authRequest.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authRequest.getName(), user.getAuthority());

        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/account")
    public ResponseEntity<UserDetailsResponse> getAccountInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userService.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not logged in"));

            return ResponseEntity.ok(UserDetailsResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .authority(user.getAuthority())
                    .build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}


