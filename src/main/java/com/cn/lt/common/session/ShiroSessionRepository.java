package com.cn.lt.common.session;

import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;

/**
 * custom shiro session manager interface
 * From：<a href="http://www.zyiqibook.com">在一起：技术分享_源码分享平台</a> 欢迎到网站进行交流
 * @author michael
 */
public interface ShiroSessionRepository {

    void saveSession(Session session);

    void deleteSession(Serializable sessionId);

    Session getSession(Serializable sessionId);

    Collection<Session> getAllSessions();
}
