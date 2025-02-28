package com.example.user.core.ports.outgoing;

import jakarta.transaction.Transactional;
import com.example.user.core.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    @Transactional
    User persist(User u);

    @Transactional
    void delete(Long id);

    @Transactional
    List<User> findAllUsers();

    @Transactional
    Optional<User> findOneById(Long id);

    @Transactional
    User findByNameAndPassword(String name, String password);

    @Transactional
    Optional<User> findByName(String name);

    @Transactional
    void update(User user);

}
