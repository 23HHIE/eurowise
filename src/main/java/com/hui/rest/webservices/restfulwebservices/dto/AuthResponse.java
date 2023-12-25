package com.hui.rest.webservices.restfulwebservices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {


    private Long id;

    private String username;
    private String email;

    private String profile;

    //0:active, 1:disabled, 2:delete
    private int status;
    private String role;

    //private int credits;
    //
    //private int level;
    //
    //private String headShotUrl;


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ctime;

    //private String sessionId;

    private String token;

}