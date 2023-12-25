package com.hui.rest.webservices.restfulwebservices.dto;


import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}