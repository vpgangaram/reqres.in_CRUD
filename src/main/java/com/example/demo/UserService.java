package com.example.demo;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface UserService {
    List<User> getAllUsers();

    
    String createUser(createUser user);


    String updateUser(Long id, createUser user);


    ResponseEntity<String> deleteUser(Long id);
}
