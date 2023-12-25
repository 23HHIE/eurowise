package com.hui.rest.webservices.restfulwebservices.expense.repository;


import com.hui.rest.webservices.restfulwebservices.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <User, Long>{

    User findByNameAndPassword(String username, String password);


    User findByName(String username);

    List<User> findAllByStatus(int status);
}