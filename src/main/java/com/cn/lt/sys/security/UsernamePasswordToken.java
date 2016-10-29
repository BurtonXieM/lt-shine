package com.cn.lt.sys.security;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by XieM on 2016/10/29.
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken implements AuthenticationToken {
    private static final long serialVersionUID = -83465349342459576L;
    private String memberType;

    public UsernamePasswordToken(String username, String password) {
        super(username, password);
    }


    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }


}
