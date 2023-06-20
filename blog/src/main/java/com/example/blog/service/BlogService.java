package com.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blog.bean.Blog;
import com.example.blog.dto.Result;
import org.springframework.web.multipart.MultipartFile;

public interface BlogService extends IService<Blog> {
    Result getPage(Integer currentPage);

    Result editBlog(Blog blog);

    Result createBlog(Blog blog);

    Result deleteBlog(Integer blogId);

    Result uploadImg(Integer blogId, MultipartFile file);

    Result downloadImg(Long blogId, String filename);
}
