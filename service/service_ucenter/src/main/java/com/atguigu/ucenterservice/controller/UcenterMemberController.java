package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-05-11
 */
@RestController
@RequestMapping("/ucenterservice/ucenter-member")
@CrossOrigin
public class UcenterMemberController {


    @Autowired
    private UcenterMemberService memberService;


    //登录
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){
        //返回token值,使用jwt
        String token= memberService.login(loginVo);
        return R.ok().data("token",token);
    }


    //注册
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    //根据token获取用户信息
    @GetMapping("getUserInfoForJwt")
    public R getMemberInfo(HttpServletRequest request){
        String methodId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库用户id获取用户信息
        UcenterMember member = memberService.getById(methodId);
        return R.ok().data("userInfo",member);
    }
}



















