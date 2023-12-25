package com.hui.rest.webservices.restfulwebservices.security;

import com.hui.rest.webservices.restfulwebservices.dto.AuthResponse;
import com.hui.rest.webservices.restfulwebservices.dto.LoginRequest;
import com.hui.rest.webservices.restfulwebservices.dto.RegisterRequest;
import com.hui.rest.webservices.restfulwebservices.service.AuthService;
import com.hui.rest.webservices.restfulwebservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
//@RequestMapping("/auth")
public class JwtAuthController {

    //private final UserService userService;

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public Response register(@RequestBody RegisterRequest registerRequest){

        AuthResponse authResponse = authService.register(registerRequest);
        return Response.success(authResponse);
    }



    @PostMapping("/authenticate")
    public Response<AuthResponse> login(@RequestBody LoginRequest loginRequest){

        return Response.success(authService.login(loginRequest));

    }


    @PostMapping("/auth/logout")
    public Response logout(@RequestParam  String sessionId){

        return Response.success(null);
    }
}