package com.hui.rest.webservices.restfulwebservices.security;

import lombok.Data;

@Data
public class Response<T> {
    private int status;

    private String message;

    private T data;

    private int totalPages;
    private Long totalElements;

    private int page;

    private Response(int status, String message, T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private Response(T data, int totalPages, Long totalElements, int page){
        this.status = 200;
        this.message = "success";
        this.data = data;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.page = page;
    }

    public static<T> Response<T> success(T data){
        return new Response(200, "success", data);
    }

    public static<T> Response<T> success(T data, int totalPages, Long totalElements, int page){
        return new Response(data, totalPages, totalElements, page);
    }
}
