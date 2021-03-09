package com.example.jwtdemo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwtdemo.domain.bo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;

@Repository
@Slf4j
public class JwtUtils {

    public static Algorithm algorithm = Algorithm.HMAC256("SECRET");

    public String getToken(User user){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);
        Date exp = calendar.getTime();
        log.info(exp.toString());
        String token = JWT.create()
                .withAudience(user.getUsername())
                .withExpiresAt(exp)
                .withClaim("username", user.getUsername())
                .sign(JwtUtils.algorithm);
        log.info(token);
        return token;
    }

}
