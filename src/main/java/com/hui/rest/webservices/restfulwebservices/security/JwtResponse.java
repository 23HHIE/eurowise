package com.hui.rest.webservices.restfulwebservices.security;

import lombok.Data;

@Data
public class JwtResponse <T>{
    private int status;

    private String message;

    private T data;

    private int totalPages;
    private Long totalElements;

    private int page;

    private JwtResponse(int status, String message, T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private JwtResponse(T data, int totalPages, Long totalElements, int page){
        this.status = 200;
        this.message = "success";
        this.data = data;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = page;
    }

    public static<T> JwtResponse<T> success(T data){
        return new JwtResponse<>(200, "success", data);
    }

    public static<T> JwtResponse<T> success(T data, int totalPages, Long totalElements, int page){
        return new JwtResponse<>(data, totalPages, totalElements, page);
    }
}
