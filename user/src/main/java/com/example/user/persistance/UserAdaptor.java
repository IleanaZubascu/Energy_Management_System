package com.example.user.persistance;

import com.example.user.core.ports.outgoing.UserRepository;
import com.example.user.persistance.model.UserMapper;
import com.example.user.persistance.repository.UserPsqlRepository;
import com.example.user.core.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAdaptor implements UserRepository {

    private final UserMapper userMapper;
    private final UserPsqlRepository userRepository;

    public UserAdaptor(UserMapper userMapper, UserPsqlRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public User persist(User u) {
        return userMapper.fromEntity(userRepository.save(userMapper.fromDomain(u)));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userMapper.fromEntities(userRepository.findAll());
    }

    @Override
    public Optional<User> findOneById(Long id) {
        return userRepository.findById(id).map(userMapper::fromEntity);
    }

    @Override
    public User findByNameAndPassword(String name, String password) {

        return userRepository.findByNameAndPassword(name, password)
                .map(userMapper::fromEntity)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name).map(userMapper::fromEntity);
    }

    @Override
    public void update(User user) {
        userRepository.updateUserName(user.getId(), user.getName());
    }
}
