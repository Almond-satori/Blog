package com.example.blog.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_user")
public class User {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String username;

    private String avatar;

    private String email;

    private String password;

    private Integer status;

    private Timestamp created;

    private Timestamp lastLogin;
}
