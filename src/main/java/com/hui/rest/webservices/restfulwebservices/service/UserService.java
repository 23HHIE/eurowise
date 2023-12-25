package com.hui.rest.webservices.restfulwebservices.service;

import com.hui.rest.webservices.restfulwebservices.user.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    List<User> findAll();
    List<User> findAll(Pageable pageable);

    User findById(Long id);

    boolean exist(String username);

    User findByUsername(String username);
}
