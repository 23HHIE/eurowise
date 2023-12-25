package com.hui.rest.webservices.restfulwebservices.user;


import com.hui.rest.webservices.restfulwebservices.expense.ExpenditureList;
import com.hui.rest.webservices.restfulwebservices.expense.repository.ExpenseRepository;
import com.hui.rest.webservices.restfulwebservices.expense.repository.UserRepository;
import com.hui.rest.webservices.restfulwebservices.security.JwtService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//处理与用户相关的HTTP请求（）,将请求传递给相应的服务层来执行业务逻辑，然后返回响应给客户端
@RestController
public class UserJpaResource {
    @Autowired
    private UserRepository repository;
    private ExpenseRepository expenseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtService jwtService;

    public UserJpaResource(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {

        return repository.findAllByStatus(0);
    }


    @GetMapping("/jpa/users/{id}")
    public User retrieveUser(@PathVariable long id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("id:" + id);
        }

        EntityModel<User> entityModel = EntityModel.of(user.get());

        WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());

        entityModel.add(link.withRel("all- users"));

        return user.get();
    }


    @GetMapping("jpa/users/{id}/expense-lists")
    public List<ExpenditureList> retrieveExpenseListsForUser(@PathVariable long id) {
        Optional<User> user = repository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException("id:" + id);
        }

        return user.get().getExpense();
    }


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {

        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (password == null || password.trim().isEmpty()) {
            // 处理密码为 null 或空的情况，可以返回相应的错误信息或者抛出异常
            System.out.println("Password cannot be null or empty");
            //return ResponseEntity.badRequest().body("Password cannot be null or empty");
        }

        //String encodedPassword = passwordEncoder.encode(password);
        //user.setPassword(encodedPassword);

        User savedUser = repository.save(user);

        // 构建一个URI位置返回给客户端
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();


    }


    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        user = repository.findByName(user.getUsername());
        String jwtToken = jwtService.generateToken(user);
        user.setToken(jwtToken);
        return new ResponseEntity(user, HttpStatusCode.valueOf(200));
    }


    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable long id) {
        repository.deleteById(id);
    }



    @PutMapping("/edit/users/{id}")
    public ResponseEntity<User> modifyUser(@PathVariable long id, @RequestBody User user) {


        try {

            //String newPwd = passwordEncoder.encode(user.getPassword());
            //user.setPassword(newPwd);
            Optional<User> optionalUser = repository.findById(id);

            if (optionalUser.isPresent()) {

                User existingUser = optionalUser.get();


                // 更新非空字段（排除密码）
                if (user.getName() != null) {
                    existingUser.setName(user.getName());
                }
                if (user.getEmail() != null) {
                    existingUser.setEmail(user.getEmail());
                }
                if (user.getPassword() != null) {

                    String encodedPassword = passwordEncoder.encode(user.getPassword());
                    existingUser.setPassword(encodedPassword);

                }
                if (user.getRole() != null) {
                    existingUser.setRole(user.getRole());
                }

                User modifiedUser = repository.save(existingUser);
                return ResponseEntity.ok(modifiedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // 处理异常
            System.out.println("Error modifying user:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PostMapping("users/{id}/expenses")
    public ResponseEntity<Object> createExpenseForUser(@PathVariable long id,
                                                       @Valid @RequestBody ExpenditureList expense) {
        Optional<User> user = repository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException("id:" + id);
        }

        expense.setUser(user.get());


        ExpenditureList savedExpense = expenseRepository.save(expense);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedExpense.getId())
                .toUri();
        return ResponseEntity.created(location).build();

    }

}

