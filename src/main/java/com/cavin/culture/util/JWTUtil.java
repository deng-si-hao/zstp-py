package com.cavin.culture.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class JWTUtil {

    private final static Key key;

    static {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public static String getJwtToken(String username) {
        // JWT的生成时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // JWT的id
        String timestampId = String.valueOf(now.getTime()) + username;

        // 生成JWT
        JwtBuilder builder = Jwts.builder()
                .setId(timestampId)
                .setIssuedAt(now)
                .setSubject("UserInfo")
                .setIssuer("HHHHHjjw")
                .setAudience(username)
                .signWith(key);

        String token = builder.compact();
        return token;
    }

    public static Claims parseToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }

    /**
    * 生成id
    * */
    public static String getNewId() {
        //获取UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        //生成后缀
        long suffix = Math.abs(uuid.hashCode() % 100000000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String time = sdf.format(new Date(System.currentTimeMillis()));
        //生成前缀
        long prefix = Long.parseLong(time) * 100000000;
        String userId = String.valueOf(prefix + suffix);
        return userId;
    }

}
