package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.bean.User;

public interface UserService extends IService<User> {
    User getUserById(Long id);
}
