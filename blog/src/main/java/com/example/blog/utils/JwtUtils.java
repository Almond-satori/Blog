package com.example.blog.utils;

import io.jsonwebtoken.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Data
@Component
@ConfigurationProperties("almond.jwt") //用于获取在Application.yaml中的配置信息
public class JwtUtils {
    private String secret;
    private long expire;
    private String header;

    /**
     * 根据用户id生成对应的JwtToken
     * @param userId
     * @return
     */
    public String generateJwtToken(long userId){
        Date current = new Date();
        Date expireDate = new Date(current.getTime() + expire * 1000);
        return Jwts.builder()
                // 设置标头参数
                .setHeaderParam("typ", "JWT")
                // 设置payload主体内容
                .setSubject(userId + "")
                .setIssuedAt(current)
                .setExpiration(expireDate)
                // 设置签名部分
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    /**
     * 解析jwtToken
     * @param token
     * @return
     */
    public Claims getClaimsByToken(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("getClaimsByToken failed,check the token");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断token是否过期
     * @param expiration
     * @return true代表过期
     */
    public boolean isTokenExpire(Date expiration){
        return expiration.before(new Date());
    }
}
