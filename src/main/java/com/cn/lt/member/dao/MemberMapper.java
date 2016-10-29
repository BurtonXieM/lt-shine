package com.cn.lt.member.dao;


import com.cn.lt.member.entity.Member;

public interface MemberMapper {
    public int deleteByPrimaryKey(Integer id) ;

    public int insert(Member record) ;

    public int insertSelective(Member record);

    public Member selectByPrimaryKey(Integer id) ;

    public int updateByPrimaryKeySelective(Member record);

    public int updateByPrimaryKey(Member record) ;

    public Member findByName(String loginName);

    public int updatePassword(Member member);

}