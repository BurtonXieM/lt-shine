package com.cn.lt.member.controller;

import com.cn.lt.member.dao.MemberMapper;
import com.cn.lt.member.entity.Member;
import com.cn.lt.sys.security.UsernamePasswordToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by admin on 2016/10/23.
 */
@Controller
@RequestMapping("member")
public class MemberController {


    @Resource
    MemberMapper memberMapper ;


    @RequestMapping("index")
    public String index(){
        Member  member =  memberMapper.selectByPrimaryKey(1);

        System.out.println(member.getLoginName());
        return "member/index" ;
    }

    /***
     * 模拟登陆
     * @return
     */
    @RequestMapping("login")
    @ResponseBody
    public String login(){

        String loginName = "weihua123" ;
        String password = "wk1234" ;

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginName,password) ;
        try {
            subject.login(token);
        } catch (Exception e) {
            //logger.error("error:{}", e);
            e.printStackTrace();
            return "abc";
        }
        return "success" ;
    }
    @RequestMapping("test")
    public String test(){

        return "member/index" ;
    }
}
