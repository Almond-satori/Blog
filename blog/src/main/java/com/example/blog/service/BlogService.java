package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.bean.Blog;
import com.example.blog.dto.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface BlogService extends IService<Blog> {
    Result getPage(Integer currentPage);

    Result editBlog(Blog blog);

    Result createBlog(Blog blog);

    Result deleteBlog(Long blogId);

    Result uploadImg(MultipartFile file);

    ResponseEntity<byte[]> downloadImg(String filename);
}
