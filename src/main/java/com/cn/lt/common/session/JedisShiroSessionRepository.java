package com.cn.lt.common.session;

import com.cn.lt.common.util.JedisManager;
import com.cn.lt.common.util.SerializeUtil;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.session.Session;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Timothy
 * @version 2015-12-04
 */

public class JedisShiroSessionRepository implements ShiroSessionRepository {
    public static final Logger logger = LoggerFactory.getLogger(JedisShiroSessionRepository.class);

    private static final String REDIS_SHIRO_SESSION = "shiro_session";

    private static final int DB_INDEX = 0;

    private JedisManager jedisManager;

    public void saveSession(Session session) {
        if(session == null || session.getId() == null){
            throw new NullPointerException("Session is empty");
        }
        try {
            byte[] key = SerializeUtil.serialize(buildRedisSessionKey(session.getId()));
            byte[] value = SerializeUtil.serialize(session);
            long sessionTimeOut = session.getTimeout() / 1000;
            Long expireTime = sessionTimeOut + (5 * 60);// + SESSION_VAL_TIME_SPAN + (5 * 60);
            getJedisManager().saveValueByKey(DB_INDEX, key, value, expireTime.intValue());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error:{}", e);
        }

    }

    public void deleteSession(Serializable sessionId) {
        if (sessionId == null) {
            throw new NullPointerException("session id is empty");
        }
        try {
            getJedisManager().deleteByKey(DB_INDEX,
                    SerializeUtil.serialize(buildRedisSessionKey(sessionId)));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error:{}", e);
        }
    }

    public Session getSession(Serializable sessionId) {
        if (sessionId == null)
            throw new NullPointerException("session id is empty");
        Session session = null;
        try {
            byte[] value = getJedisManager().getValueByKey(DB_INDEX, SerializeUtil
                    .serialize(buildRedisSessionKey(sessionId)));
            session = SerializeUtil.deserialize(value, Session.class);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error:{}", e);
        }
        return session;
    }

    public Collection<Session> getAllSessions() {
        logger.info("get all sessions");
        try {
            Set<byte[]> keys = jedisManager.keys("*session*");
            if (!CollectionUtils.isEmpty(keys)) {
                List<Session> values = new ArrayList<Session>(keys.size());
                for (byte[] key : keys) {
                    Session value = SerializeUtil.deserialize(getJedisManager().getValueByKey(DB_INDEX,key),Session.class);
//                    logger.info("get all sessions---活动session---" + value.getId());
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    private String buildRedisSessionKey(Serializable sessionId) {
        return REDIS_SHIRO_SESSION + sessionId;
    }

    public JedisManager getJedisManager() {
        return jedisManager;
    }

    public void setJedisManager(JedisManager jedisManager) {
        this.jedisManager = jedisManager;
    }
}

