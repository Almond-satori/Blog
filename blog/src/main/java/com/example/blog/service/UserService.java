package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.bean.User;
import com.example.blog.dto.LoginDto;
import com.example.blog.dto.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface UserService extends IService<User> {
    Result getUserInfoById(Long id);

    Result login(LoginDto loginDTO, HttpServletResponse response);

    User getUserById(Long id);

    Result register(User user);

    Result uploadAvatar(MultipartFile file,Long userId);

    ResponseEntity<byte[]> getAvatar(Long userId);
}


