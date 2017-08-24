package com.example.demo.service;


import com.example.demo.model.User;

/**
 * Created by LynAs on 20-Mar-16
 */
public interface UserService {
    User loadUserByUsername(String username);

    long post(User User);

    User get(long id);

    User patch(User User);

    boolean delete(long id);
}
