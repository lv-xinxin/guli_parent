package com.atguigu.ucenterservice.service.impl;

import com.atguigu.commonutils.JwtUtils;

import com.atguigu.commonutils.MD5;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-05-11
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;



    //登录
    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //校验参数
        if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)){
            throw new GuliException(20001,"error");
        }

        //获取会员
        UcenterMember member = baseMapper.selectOne(new QueryWrapper<UcenterMember>().eq("mobile",mobile));
        if(null == member){
            throw new GuliException(20001,"error");
        }

        //校验密码
        if(!MD5.encrypt(password).equals(member.getPassword())){
            throw new GuliException(20001,"error");
        }

        //检验是否被禁用
        if(member.getIsDeleted()){
            throw new GuliException(20001,"error");
        }

        //使用token生成token字符串
        String token = JwtUtils.getJwtToken(member.getId(),member.getNickname());
        return token;
    }
    //注册
    @Override
    public void register(RegisterVo registerVo) {

        String code = registerVo.getCode();//验证码
        String mobile = registerVo.getMobile();//手机号
        String nickname = registerVo.getNickname();//昵称
        String password = registerVo.getPassword();//密码

        //校验参数
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)
                ||StringUtils.isEmpty(mobile) ||StringUtils.isEmpty(password)){
            throw new GuliException(20001,"注册失败");
        }

        //判断验证码Todo
        //1.先获取redis中生成的验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode) || redisCode.isEmpty()) {
            throw new GuliException(20001,"注册失败");
        }

        //判断手机号是否重复，表里面存在相同手机号不进行添加
        Integer count = checkphone(registerVo);
        if(count > 0){
            throw new GuliException(20001,"注册失败");
        }

        //数据添加数据库中
        UcenterMember member=new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));
        member.setIsDisabled(false);
        member.setAvatar("https://tenfei04.cfp.cn/creative/vcg/800/version23/VCG41175510742.jpg");
        baseMapper.insert(member);
    }

    @Override
    public UcenterMember getUserByOpenId(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    public Integer checkphone(RegisterVo registerVo){
        Integer count = baseMapper.selectCount(new QueryWrapper<UcenterMember>().eq("mobile",registerVo.getMobile()));
        return count;
    }



}































