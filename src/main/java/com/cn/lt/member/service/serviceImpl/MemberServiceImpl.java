package com.cn.lt.member.service.serviceImpl;

import com.cn.lt.member.dao.MemberMapper;
import com.cn.lt.member.entity.Member;
import com.cn.lt.member.service.MemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by XieM on 2016/10/29.
 */
@Service("memberService")
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberMapper memberMapper;
    @Override
    public List<Member> findUserByLoginName(String loginName) {
        return memberMapper.findUserByLoginName(loginName);
    }
}
