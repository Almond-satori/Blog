package com.example.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.bean.Blog;
import com.example.blog.dto.Result;
import com.example.blog.mapper.BlogMapper;
import com.example.blog.service.BlogService;
import com.example.blog.shiro.UserInfo;
import com.example.blog.utils.UserHolder;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    private UserHolder userHolder;

    @Override
    public Result getPage(Integer currentPage) {
        // 非法查询定位到第一页
        if(currentPage < 1 || currentPage == null) currentPage = 1;

        Page<Blog> page = new Page(currentPage, 5,0);
        page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.success(page);
    }

    @Override
    public Result editBlog(Blog blog) {
        //        System.out.println(blog.toString());
        // 获取当前访问的用户id
        UserInfo userInfo = userHolder.getUserInfo();

        Blog newBlog = new Blog();
        // 验证修改权限
        if(blog.getId() == null){
            return Result.fail("博客id异常");
        }else{
            // 修改现有博客
            Blog dataBlog = getById(blog.getId());
            Assert.notNull(dataBlog.getId(), "不能修改一个不存在的博客");
            Assert.isTrue(userInfo.getId().equals(dataBlog.getUserId()),"不能编辑别人的博客");
            newBlog.setUserId(dataBlog.getUserId());
        }
        // 设置状态
        newBlog.setStatus(0);
        newBlog.setCreated(LocalDateTime.now());
        BeanUtil.copyProperties(blog, newBlog, "status","created","userId");

        saveOrUpdate(newBlog);
        return Result.success("保存博客成功");
    }

    @Override
    public Result createBlog(Blog blog) {
        UserInfo userInfo = userHolder.getUserInfo();
        Assert.isTrue(blog.getUserId().equals(userInfo.getId()),"只能创建自己的博客");

        blog.setId(null);
        blog.setCreated(LocalDateTime.now());
        blog.setStatus(0);
        save(blog);
        return Result.success("创建博客成功");
    }

    @Override
    public Result deleteBlog(Integer blogId) {
        UserInfo userInfo = userHolder.getUserInfo();
        Blog dataBlog = getById(blogId);

        if(dataBlog.getId() == null) return Result.fail("不存在当前博客");
        Assert.isTrue(dataBlog.getUserId().equals(userInfo.getId()),"只能删除自己的博客");

        removeById(blogId);
        return Result.success("成功删除博客");
    }
}
