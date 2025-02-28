package com.example.user.core.ports.incoming;

import com.example.user.constants.AuthoritiesConstants;
import com.example.user.core.model.User;
import com.example.user.core.ports.outgoing.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserFacade implements UserService {


    private final UserRepository userRepository;

    public UserFacade(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User save(User user) {
        user.setAuthority(AuthoritiesConstants.USER);
        return userRepository.persist(user);
    }

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findOneById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAllUsers();
    }

    @Override
    public User login(String username, String password) {
        return userRepository.findByNameAndPassword(username, password);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }
}
