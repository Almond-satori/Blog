package com.example.blog.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.example.blog.bean.User;
import com.example.blog.service.UserService;
import com.example.blog.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    /**
     * 在执行认证前，判断token是否是JwtToken，以便后续强转
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }

    /**
     * 认证方法,检查前端访问接口时是否带有合法的用户token（注意这里不做用户密码校验，仅仅校验用户token是否合法）
     * @param authenticationToken 前端传递来的token
     * @return authenticationInfo 正确的用户信息
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken token = (JwtToken) authenticationToken;
        String userId = jwtUtils.getClaimsByToken((String) token.getPrincipal()).getSubject();
        User user = userService.getUserById(Long.parseLong(userId));
        // token不合法 1.用户不存在 2.用户被锁定
        if(user == null){
            throw new AccountException("用户不存在");
        }
        if(user.getStatus() == -1){
            throw new AccountException("用户被锁定");
        }
        // 封装一个 用户的数据传输对象
        UserInfo userInfo = new UserInfo();
        BeanUtil.copyProperties(user, userInfo);

        // 返回SimpleAuthenticationInfo代表校验成功
        // 返回null代表校验失败(这里直接抛出异常了)
        return new SimpleAuthenticationInfo(userInfo,token.getCredentials(),getName());
    }
}
