package com.example.blog.shiro;

import cn.hutool.json.JSONUtil;
import com.example.blog.dto.Result;
import com.example.blog.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            throw new ExpiredCredentialsException("jwt 认证错误或已经过期");
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

    /**
     * 前置处理跨域问题,向请求端设置Response Header(响应头部)的Access-Control-Allow-Origin属性声明允许跨域访问。
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);

        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }


}
