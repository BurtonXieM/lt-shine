package com.cn.lt.sys.security;

import com.cn.lt.member.entity.Member;
import com.cn.lt.member.service.MemberService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/*import com.cn.lt.back.member.entity.MemberBusinessDetail;
import com.cn.lt.back.member.openService.OpenMemberService;
import com.cn.lt.back.member.service.MemberBusinessDetailService;
import com.cn.lt.front.cart.service.CartService;*/


@Service
@Transactional
public class MyAuthorizingRealm extends AuthorizingRealm {

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(MyAuthorizingRealm.class);

    @Autowired
    private MemberService memberService;
   /* @Autowired
    private CartService cartService;
    @Autowired
    private OpenMemberService openMemberService;
    @Autowired
    private MemberBusinessDetailService memberBusinessDetailService;*/

    /**
     * 权限认证
     *//*
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)  {
        //获取登录时输入的用户名
        Principal principal = (Principal) getAvailablePrincipal(principalCollection);
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        List<Member> memberList = memberService.findUserByLoginName(principal.getLoginName());
        if(memberList.size() == 1){
            Member member = memberList.get(0);
            if( !"1".equals(member.getMemberType()) && "1".equals(member.getStatus()) ){
                info.addRole("seller");
            }else{
                info.addRole("buyer");
            }
        }else{
            info.addRole("buyer");
        }
        *//*MemberBusinessDetail memberBusinessDetail = memberBusinessDetailService.selectByMemberId(principal.getId());
        if(memberBusinessDetail != null && memberBusinessDetail.getIsEdit() != null && "1".equals(memberBusinessDetail.getIsEdit())){
            info.addRole("canChangeBusiInfo");
        }*//*
        return info;
    }*/

    /**
     * 登录认证;
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = (String) token.getPrincipal();
        List<Member> memberList = memberService.findUserByLoginName(username);

        if (memberList.size() != 1) {
            throw new UnknownAccountException();//没找到帐号
        }
        Boolean isTrue = null;
        /*try {
            isTrue = openMemberService.checkAccountStatusByLoginName(username);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error:{}",e.getMessage());
            throw new DisabledAccountException(e.getMessage());
        }*/
        //检查用户账号状态，
        /*if(!isTrue) {
            throw new LockedAccountException("账号被冻结");

        }*/
        Member member = memberList.get(0);
        if("5".equals(member.getMemberType()) || "6".equals(member.getMemberType()) || "7".equals(member.getMemberType())) {
            throw new DisabledAccountException("运营账号不能登陆");
        }
        //Integer cartId = cartService.findCartId(member.getId());
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                new Principal(member),//用户名
                member.getLoginPassword(), //密码
                ByteSource.Util.bytes(member.getSalt()),
                getName()  //realm name
        );

        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

    public static class Principal implements Serializable {

        private static final long serialVersionUID = 1015893929363968545L;

        private int id;

        private String loginName;

        private String phone;

        private Integer cartId;

        private String email;

        private String memberType;

        private String status;


        public Principal(Member member) {
            this.id = member.getId();
            this.loginName = member.getLoginName();
            this.phone = member.getPhone();
            //this.cartId = cartId;
            this.email = member.getEmail();
            this.memberType = member.getMemberType();
            this.status = member.getStatus();
        }

        public String getMemberType() {
            return memberType;
        }

        public String getStatus() {
            return status;
        }

        public int getId() {
            return id;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getPhone() {
            return phone;
        }

        public Integer getCartId() {
            return cartId;
        }

        public String getEmail() {
            return email;
        }

    }
}