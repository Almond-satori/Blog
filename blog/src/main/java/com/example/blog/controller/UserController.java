package com.example.blog.controller;

import com.example.blog.bean.User;
import com.example.blog.dto.LoginDto;
import com.example.blog.dto.Result;
import com.example.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户的部分非敏感信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getUser(@PathVariable("id") Long id){
        return userService.getUserInfoById(id);
    }


    @PostMapping("/register")
    public Result save(@Validated @RequestBody User user){
        return userService.register(user);
    }

    @CrossOrigin
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDTO, HttpServletResponse response){
        Result login = userService.login(loginDTO, response);
        return login;
    }

    @GetMapping("/logout")
    @RequiresAuthentication
    public Result logout(){
        SecurityUtils.getSubject().logout();
        return Result.success();
    }

}
