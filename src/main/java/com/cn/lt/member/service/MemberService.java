package com.cn.lt.member.service;

import com.cn.lt.member.entity.Member;

import java.util.List;

/**
 * Created by XieM on 2016/10/29.
 */
public interface MemberService {

    public List<Member> findUserByLoginName(String loginName);
}
