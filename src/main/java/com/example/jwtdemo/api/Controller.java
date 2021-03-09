package com.example.jwtdemo.api;

import com.example.jwtdemo.domain.bo.User;
import com.example.jwtdemo.jwt.JwtUtils;
import com.example.jwtdemo.jwt.UserLoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class Controller {

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/token")
    public String login(String username, String password){
        return jwtUtils.getToken(new User(username, password));
    }

    @PostMapping("/auth")
    @UserLoginToken
    public String auth(){
        return "pass";
    }

    @GetMapping("/get")
    public String get(String username, String password){
        return jwtUtils.getToken(new User(username, password));
    }

}
