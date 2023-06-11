package com.example.blog.shiro;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDTO {

    private Long id;

    private String username;

    private String avatar;

    private String email;

    private Integer status;

    private Timestamp created;

    private Timestamp lastLogin;
}
