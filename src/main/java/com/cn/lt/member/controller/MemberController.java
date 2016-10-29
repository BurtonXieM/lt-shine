package com.cn.lt.member.controller;

import com.cn.lt.member.dao.MemberMapper;
import com.cn.lt.member.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
