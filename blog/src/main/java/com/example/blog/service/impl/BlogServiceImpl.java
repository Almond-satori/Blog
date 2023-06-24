package com.example.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blog.bean.Blog;
import com.example.blog.dto.Result;
import com.example.blog.mapper.BlogMapper;
import com.example.blog.service.BlogService;
import com.example.blog.shiro.UserInfo;
import com.example.blog.utils.FileUploadUtil;
import com.example.blog.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    private UserHolder userHolder;

    @Value("${almond.img-path}")
    private String IMG_PATH;

    @Value("${almond.server-path}")
    private String SERVER_PATH;

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
    public Result deleteBlog(Long blogId) {
        UserInfo userInfo = userHolder.getUserInfo();
        Blog dataBlog = getById(blogId);

        if(dataBlog.getId() == null) return Result.fail("不存在当前博客");
        Assert.isTrue(dataBlog.getUserId().equals(userInfo.getId()),"只能删除自己的博客");

        removeById(blogId);
        return Result.success("成功删除博客");
    }

    /**
     * 在博客中插入图片,存储图片,将获取图片的url返回给前端
     * @param file
     * @return
     */
    @Override
    public Result uploadImg(MultipartFile file) {



        String originalFilename = file.getOriginalFilename();
        if(originalFilename == null) return Result.fail("文件名异常");

        String[] pair = FileUploadUtil.divideFileName(originalFilename);

        String uuid = UUID.randomUUID().toString();
        String newFileName = pair[0] + "-" + uuid + "." + pair[1];
        String pathPrefix = IMG_PATH;

        //在指定位置创建文件
        FileUploadUtil.createAndTransferFile(pathPrefix,newFileName, file);

        // 向前端传回找到该图片的url
        return Result.success("成功创建图片",
                SERVER_PATH + "blog/image/" + newFileName);
    }

    /**
     * 将图片返回
     * @param filename 文件名
     * @return
     */
    @Override
    public ResponseEntity<byte[]> downloadImg(String filename) {

        String fileLocation = IMG_PATH + filename;
        byte[] bytes = FileUtil.readBytes(fileLocation);

        return ResponseEntity.ok(bytes);
    }
}
