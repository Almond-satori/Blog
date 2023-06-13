package com.example.blog.shiro;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class UserInfo {

    private Long id;

    private String username;

    private String avatar;

    private String email;

    private Integer status;

    private LocalDateTime created;

    private LocalDateTime lastLogin;
}
