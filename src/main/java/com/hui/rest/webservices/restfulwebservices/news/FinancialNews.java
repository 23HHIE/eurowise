package com.hui.rest.webservices.restfulwebservices.news;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class FinancialNews {

    @GeneratedValue
    private String id;
    private String headline;
    //private String summary;
    private String url;


}
