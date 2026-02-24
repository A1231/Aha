package com.aha.aha.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aha.aha.entity.User;
import com.aha.aha.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String name, boolean isHost, Long roomId) {
        Long userId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        User user = new User(userId, name, 0, isHost, roomId);
        
        User savedUser = userRepository.save(user);
        return savedUser;
    }



}
