package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ksb on 2017. 6. 4..
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
