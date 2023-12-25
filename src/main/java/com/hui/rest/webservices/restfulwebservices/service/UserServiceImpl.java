package com.hui.rest.webservices.restfulwebservices.service;

import com.hui.rest.webservices.restfulwebservices.expense.repository.UserRepository;
import com.hui.rest.webservices.restfulwebservices.user.User;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).toList();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public boolean exist(String username) {
        User user = userRepository.findByName(username);
        return user != null;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByName(username);
    }


}