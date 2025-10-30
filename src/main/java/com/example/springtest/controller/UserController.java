package com.example.springtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.springtest.pojo.User;
import com.example.springtest.Service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUserById")
    public User getUserById(Integer id){
        return userService.getUserById(id);
    }

}
