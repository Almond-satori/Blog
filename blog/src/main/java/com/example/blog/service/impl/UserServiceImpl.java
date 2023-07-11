package com.example.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
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
import com.example.blog.utils.FileUploadUtil;
import com.example.blog.utils.JwtUtils;
import com.example.blog.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserHolder userHolder;

    @Value("${almond.avatar-path}")
    private String AVATAR_PATH;


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
        response.setHeader("Access-Control-Expose-Headers","Authorization");

        return Result.success(new MapUtil().builder()
                .put("id", res.getId())
                .put("username",res.getUsername())
                .put("email",res.getEmail())
                .map()
        );
    }

    /**
     * 设置允许Authorization跨域:
     * 如果在跨域请求中需要访问响应头中的自定义字段，例如Authorization，
     * 那么除了设置Access-Control-Allow-Headers以允许该字段之外，
     * 还需要设置Access-Control-Expose-Headers来将该字段暴露给前端。
     */

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


    @Override
    public Result uploadAvatar(MultipartFile file,Long userId) {
        User user = getById(userId);
        if(user == null) return Result.fail("错误,不存在该用户");

        // uuid生成文件名
        String originalFilename = file.getOriginalFilename();
        if(originalFilename == null) Result.fail("错误,文件名异常");

        String[] pair = FileUploadUtil.divideFileName(originalFilename);

        // 检查格式
        if(!"jpg".equals(pair[1])){
            Result.fail("错误,文件类型异常");
        }
        // 原文件名字prefix + uuid + 文件格式
        String uuid = UUID.randomUUID().toString();
        String filename = pair[0] + "-" + uuid + "." + pair[1];

        FileUploadUtil.createAndTransferFile(AVATAR_PATH,filename, file);

        // 更新获取头像信息的url
        user.setAvatarLocation(AVATAR_PATH + filename);
        updateById(user);

        return Result.success("上传头像成功", null);
    }

    @Override
    public ResponseEntity<byte[]> getAvatar(Long userId) {
        User user = getById(userId);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new byte[0]);
        }

        String avatarPath = user.getAvatarLocation();
        byte[] bytes = FileUtil.readBytes(avatarPath);

        return ResponseEntity.ok(bytes);
    }

    @Override
    public Result updateUser(UserDto userDto) {
        Long id = userDto.getId();
        User user = getById(id);
        if(user == null){
            return Result.fail("错误，未能查找到此用户");
        }
        user.setUsername(userDto.getUsername());
        updateById(user);
        return Result.success("成功修改用户信息", user);
    }


}
