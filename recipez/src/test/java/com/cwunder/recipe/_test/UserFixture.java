package com.cwunder.recipe._test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cwunder.recipe.user.User;
import com.cwunder.recipe.user.UserRepository;

public class UserFixture {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder pwEnc;

    public User createUser(String username, String password) {
        var user = new User(username, password, true);
        user.setPassword(pwEnc.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
