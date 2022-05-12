package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;

import com.atguigu.msmservice.service.MsmServiceImpl;
import com.atguigu.msmservice.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {

    @Autowired
    private MsmServiceImpl msmService;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone) {
        //redis获取验证码
        String code = redisTemplate.opsForValue().get(phone);

        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }
        code = RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);

        msmService.sendMsg(phone, code);

        //发送成功验证码设置redis
        //设置有效时间
        redisTemplate.opsForValue().set(phone, code, 3, TimeUnit.MINUTES);
        return R.ok().message("手机验证码短信发送成功");
    }
}

