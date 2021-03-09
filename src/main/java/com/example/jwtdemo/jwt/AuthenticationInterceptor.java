package com.example.jwtdemo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object)
        throws Exception{
        String token = request.getHeader(Constants.HEADER);
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        if(method.isAnnotationPresent(PassToken.class)){
            PassToken passToken = method.getAnnotation(PassToken.class);
            if(passToken.required()){
                return true;
            }
        }
        if(method.isAnnotationPresent(UserLoginToken.class)){
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if(userLoginToken.required()){
                if(token == null){
                    response.setStatus(401);
                    response.getWriter().flush();
                    return true;
//                    throw new RuntimeException("401");
                }
                String username;
                try{
                    username = JWT.decode(token).getAudience().get(0);
                }catch (JWTDecodeException jwtDecodeException){
                    response.setStatus(401);
                    response.getWriter().flush();
                    return true;
//                    throw new RuntimeException("401");
                }
                JWTVerifier jwtVerifier = JWT.require(JwtUtils.algorithm)
                        .acceptExpiresAt(0)
                        .build();
                try{
                    jwtVerifier.verify(token);
                }catch (JWTVerificationException jwtVerificationException){
                    response.setStatus(401);
                    response.getWriter().flush();
                    return true;
//                    throw new RuntimeException("401");
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView)
        throws Exception{
        //
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception e)
        throws  Exception{
        //
    }

}
