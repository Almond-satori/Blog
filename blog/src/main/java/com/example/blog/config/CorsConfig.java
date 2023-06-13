package com.example.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 处理跨域问题
 * 本配置在Controller层生效,还需要JwtFilter处前置处理跨域问题
 * 向请求端设置Response Header(响应头部)的Access-Control-Allow-Origin属性声明允许跨域访问。
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 设置可跨域的域名
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS") // 设置可跨域的访问方法
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

}
