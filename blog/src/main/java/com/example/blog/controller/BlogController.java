package com.example.blog.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.example.blog.bean.Blog;
import com.example.blog.dto.Result;
import com.example.blog.service.BlogService;
import com.example.blog.shiro.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/blog")
@RestController
public class BlogController {

    @Autowired
    private BlogService blogService;

    /**
     * 获取指定页的所有博客
     * @param currentPage 指定的页
     * @return
     */
    @GetMapping("/page/{currentPage}")
    public Result list(@PathVariable(name = "currentPage") Integer currentPage){
        return blogService.getPage(currentPage);
    }

    /**
     * 获取指定id的博客
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable(name = "id") Long id){
        Blog blog = blogService.getById(id);
        Assert.notNull(blog,"没有该id的博客");
        return Result.success(blog);
    }

    @RequiresAuthentication
    @PutMapping
    public Result update(@Validated @RequestBody Blog blog){
        return blogService.editBlog(blog);
    }

    @RequiresAuthentication
    @PostMapping
    public Result create(@Validated @RequestBody Blog blog){
        return blogService.createBlog(blog);
    }

    @RequiresAuthentication
    @DeleteMapping("/{blogId}")
    public Result delete(@PathVariable(name = "blogId") Long blogId){
        return blogService.deleteBlog(blogId);
    }

    @RequiresAuthentication
    @PostMapping("/image")
    public Result uploadImg(@RequestParam("file") MultipartFile file){
        return blogService.uploadImg(file);
    }

    @CrossOrigin
    @GetMapping("/image/{filename}")
    public ResponseEntity<byte[]> getImg(
                                         @PathVariable(name = "filename") String filename){
        return blogService.downloadImg(filename);
    }

}
