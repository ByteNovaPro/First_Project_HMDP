package com.hmdp.utils;

import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.hmdp.utils.RedisConstants.LOGIN_USER_KEY;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private RedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1.拿出token
        String token = request.getHeader("authorization");
        if(StrUtil.isBlank(token)){
            response.setStatus(401);
            return false;
        }
        //1.判断redis中是否存在用户
        if((redisTemplate.opsForHash().entries(LOGIN_USER_KEY+token)).isEmpty()){
            response.setStatus(401);
            return false;
        }
        //2.存在就保存在threaLocal中
        UserHolder.saveUser((UserDTO) redisTemplate.opsForValue().get(token));
        //3.放行
        return true;
    }
}
