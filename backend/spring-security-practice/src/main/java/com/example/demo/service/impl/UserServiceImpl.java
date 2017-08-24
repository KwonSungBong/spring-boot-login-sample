package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public long post(User User) {
        return userRepository.save(User).getId();
    }

    @Transactional
    @Override
    public User get(long id) {
        return userRepository.findOne(id);
    }

    @Transactional
    @Override
    public User patch(User user) {
        userRepository.save(user);
        return get(user.getId());
    }

    @Transactional
    @Override
    public boolean delete(long id) {
        userRepository.delete(id);
        return true;
    }
}
