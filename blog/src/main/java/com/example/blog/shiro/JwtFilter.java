package com.example.blog.shiro;

import cn.hutool.json.JSONUtil;
import com.example.blog.dto.Result;
import com.example.blog.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * createToken在执行shiro的login方法前被调用,并使用该token进行login
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest,
                                              ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwt)){
            return null;
        }
        return new JwtToken(jwt);
    }

    /**
     * 判断是否有权限访问
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest,
                                     ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");

        // 没有jwt，放行，交给权限管理处理
        if(StringUtils.isEmpty(jwt)){
            return true;
        }
        // 处理jwt
        Claims claims = jwtUtils.getClaimsByToken(jwt);
        if(claims == null || jwtUtils.isTokenExpire(claims.getExpiration())){
            throw new ExpiredCredentialsException("jwt 认证已经过期");
        }
        // 执行登录
        return executeLogin(servletRequest, servletResponse);
    }

    /**
     * 重写token认证失败的方法，向前端返回封装后的失败信息
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result res = Result.fail(throwable.getMessage());
        // 转换为json
        String jsonStr = JSONUtil.toJsonStr(res);
        try {
            response.getWriter().write(jsonStr);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return false;
    }
}
