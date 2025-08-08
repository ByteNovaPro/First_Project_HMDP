package com.hmdp.config;

import com.hmdp.utils.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //1.创建拦截器
        HandlerInterceptor interceptor = new LoginInterceptor();
        //2.添加拦截器
        registry.addInterceptor(interceptor)
                //3.添加拦截路径
                .addPathPatterns("/**")
                //4.添加排除路径
                .excludePathPatterns(
                        "/user/login",
                        "/user/code","/blog/hot",
                        "/shop/**",
                        "shop-type/**",
                        "/upload/**",
                        "/voucher/**"
                );
    }
}
