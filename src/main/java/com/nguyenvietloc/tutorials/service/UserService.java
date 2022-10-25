package com.nguyenvietloc.tutorials.service;

import com.nguyenvietloc.tutorials.model.User;

import java.util.List;

public interface UserService {
    void save(User user);

    User findByUsername(String username);

    List<User> all();
}
