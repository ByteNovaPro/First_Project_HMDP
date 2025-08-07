package com.hmdp.controller;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.entity.UserInfo;
import com.hmdp.service.IUserInfoService;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.query;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private IUserInfoService userInfoService;

    /**
     * 发送手机验证码
     */
    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
        // 1.检验手机号是否符合格式,如果手机号不符合格式，返回错误信息
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机号格式错误");
        }
        // 2.如果手机号符合格式,生成短信验证码,并将验证码保存到session
        String code = RandomUtil.randomNumbers(6);
        session.setAttribute(phone,code);
        // 3.发送短信验证码
        log.info("成功发送短信验证码：{}",code);
        return Result.ok();
    }

    /**
     * 登录功能
     * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session){
        //1.校验手机号是否为空
        if (loginForm.getPhone() == null) {
            return Result.fail("手机号不能为空");
        }
        //2.校验手机号是否符合格式
        if(RegexUtils.isPhoneInvalid(loginForm.getPhone())){
            return Result.fail("手机号格式错误");
        }
        //3.校验验证码是否为空
        if(loginForm.getCode() == null){
            return Result.fail("验证码不存在");
        }
        //4.校验验证码是否正确
        if(!session.getAttribute(loginForm.getPhone()).toString().equals(loginForm.getCode())){
            return Result.fail("验证码错误");
        }
        //5.校验用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", loginForm.getPhone());
        User user = userService.getOne(queryWrapper); // 调用 getOne 方法
        if(user == null){
            //6.如果不存在则创建新的用户
            userService.creatUserWithPhone(loginForm.getPhone());
        }
        //7.将用户信息保存在session中
        session.setAttribute("user",user);
        return Result.ok();
    }

    /**
     * 登出功能
     * @return 无
     */
    @PostMapping("/logout")
    public Result logout(){
        // TODO 实现登出功能
        return Result.fail("功能未完成");
    }

    @GetMapping("/me")
    public Result me(){

        // TODO 获取当前登录的用户并返回
        return Result.fail("功能未完成");
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long userId){
        // 查询详情
        UserInfo info = userInfoService.getById(userId);
        if (info == null) {
            // 没有详情，应该是第一次查看详情
            return Result.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);
        // 返回
        return Result.ok(info);
    }
}
