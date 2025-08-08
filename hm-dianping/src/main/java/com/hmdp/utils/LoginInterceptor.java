package com.hmdp.utils;

import com.hmdp.dto.UserDTO;
import com.hmdp.entity.User;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取session
        HttpSession session = request.getSession();
        //2.获取session中的用户
        Object user = session.getAttribute("user");
        //3.判断session中是否存在用户
        if(user == null){
            //4.如果不存在就报错
            response.setStatus(401);
            return false;
        }
        //5.存在就保存在threaLocal中
        UserHolder.saveUser((UserDTO) user);
        //6.放行
        return true;
    }
}
