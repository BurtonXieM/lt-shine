package com.cn.lt.member.controller;

import com.alibaba.fastjson.JSON;
import com.cn.lt.member.dao.MemberMapper;
import com.cn.lt.member.entity.Member;
import com.cn.lt.sys.security.UsernamePasswordToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
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
    @ResponseBody
    public String test(){
        SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        Object object = SecurityUtils.getSubject().getPrincipals(). getPrimaryPrincipal();
        String str = JSON.toJSONString(object);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        System.out.println(session.getId());
        System.out.println(session.getHost());
        System.out.println(session.getTimeout());
        return str ;
    }
}
