package com.example.blog.utils;

import com.example.blog.shiro.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {

    /**
     * 获取当前登录的用户信息UserInfo
     * @return UserInfo
     */
    public UserInfo getUserInfo(){
        return (UserInfo) SecurityUtils.getSubject().getPrincipal();
    }
}
