package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.bean.User;
import com.example.blog.dto.LoginDto;
import com.example.blog.dto.Result;

import javax.servlet.http.HttpServletResponse;

public interface UserService extends IService<User> {
    Result getUserInfoById(Long id);

    Result login(LoginDto loginDTO, HttpServletResponse response);

    User getUserById(Long id);
}


