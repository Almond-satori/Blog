package com.example.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.bean.User;
import com.example.blog.dto.LoginDto;
import com.example.blog.dto.Result;
import com.example.blog.dto.UserDto;
import com.example.blog.mapper.UserMapper;
import com.example.blog.service.UserService;
import com.example.blog.shiro.UserInfo;
import com.example.blog.utils.JwtUtils;
import com.example.blog.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserHolder userHolder;


    @Override
    public Result getUserInfoById(Long id) {
        User user = getById(id);
        UserDto userDto = new UserDto();
        BeanUtil.copyProperties(user, userDto);

        return Result.success("获取用户信息成功",userDto);
    }

    @Override
    public Result login(LoginDto loginDTO, HttpServletResponse response) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,loginDTO.getUsername());
        User res = this.getOne(queryWrapper);
        if(res == null){
            return Result.fail("用户名或密码错误");
        }
        String psw = SecureUtil.md5(loginDTO.getPassword());
        if(!psw.equals(res.getPassword())){
            return Result.fail("用户名或密码错误");
        }

        // 生成jwt作为登录凭证
        String token = jwtUtils.generateJwtToken(res.getId());

        response.setHeader("Authorization", token);
        /**
         * 设置允许Authorization跨域:
         * 如果在跨域请求中需要访问响应头中的自定义字段，例如Authorization，
         * 那么除了设置Access-Control-Allow-Headers以允许该字段之外，
         * 还需要设置Access-Control-Expose-Headers来将该字段暴露给前端。
         */
        response.setHeader("Access-Control-Expose-Headers","Authorization");

        return Result.success(new MapUtil().builder()
                .put("id", res.getId())
                .put("username",res.getUsername())
                .put("avatar", res.getAvatar())
                .put("email",res.getEmail())
                .map()
        );
    }

    @Override
    public User getUserById(Long id) {
        User user = getById(id);
        Assert.notNull(user.getId(),"查询的用户不存在");
        return user;
    }

    /**
     * 用户注册方法
     * @param user
     * @return
     */
    @Override
    public Result register(User user) {
        // 查看是否重名
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        User one = getOne(wrapper);
        if(one != null) {
            return Result.fail("用户名已经存在");
        }

        // 密码处理
        String psw = SecureUtil.md5(user.getPassword());
        user.setPassword(psw);

        // 设置status 和 created
        user.setStatus(0);
        user.setCreated(LocalDateTime.now());

        save(user);
        return Result.success("成功注册");
    }
}
