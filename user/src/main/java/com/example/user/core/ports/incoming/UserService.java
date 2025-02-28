package com.example.user.core.ports.incoming;


import com.example.user.core.model.User;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserService {

    @Transactional
    User save(User user);

    @Transactional
    void update(User user);

    @Transactional
    void delete(Long id);

    @Transactional
    Optional<User> findById(Long id);

    @Transactional
    List<User> findAll();

    @Transactional
    User login(String username, String password);

    @Transactional
    Optional<User> findByName(String name);

}
