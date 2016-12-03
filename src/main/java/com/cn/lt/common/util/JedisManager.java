package com.cn.lt.common.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * jedis manager
 * From：<a href="http://www.zyiqibook.com">在一起：技术分享_源码分享平台</a> 欢迎到网站进行交流
 * @author michael alter by Timothy
 */
public class JedisManager {

    private JedisPool jedisPool;

    private static final int DB_INDEX = 1;

    public Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = getJedisPool().getResource();
        } catch (Exception e) {
            throw new JedisConnectionException(e);
        }
        return jedis;
    }

    public void returnResource(Jedis jedis, boolean isBroken) {
        if (jedis == null)
            return;
        if (isBroken)
            getJedisPool().returnBrokenResource(jedis);
        else
            getJedisPool().returnResource(jedis);
    }

    /**
     * 根据key获取value
     * @param dbIndex
     * @param key
     * @return
     * @throws Exception
     */
    public byte[] getValueByKey(int dbIndex, byte[] key) throws Exception {
        Jedis jedis = null;
        byte[] result = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            result = jedis.get(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
        return result;
    }

    /**
     * 根据key获取value
     * @param key
     * @return
     * @throws Exception
     */
    public String getValueByKey(String key) throws Exception {
        Jedis jedis = null;
        String result = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(DB_INDEX);
            result = jedis.get(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
        return result;
    }

    /**
     * 根据key删除
     * @param dbIndex
     * @param key
     * @throws Exception
     */
    public void deleteByKey(int dbIndex, byte[] key) throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            jedis.del(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }

    /**
     * 根据key，删除
     * @param key
     * @throws Exception
     */
    public void deleteByKey(String key) throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(DB_INDEX);
            jedis.del(key);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }

    /**
     * 保存，字节
     * @param dbIndex
     * @param key
     * @param value
     * @param expireTime
     * @throws Exception
     */
    public void saveValueByKey(int dbIndex, byte[] key, byte[] value, int expireTime)
            throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(dbIndex);
            jedis.set(key, value);
            if (expireTime > 0)
                jedis.expire(key, expireTime);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }

    /**
     * 保存，字符串
     * @param key
     * @param value
     * @param expireTime
     * @throws Exception
     */
    public void saveValueByKey(String key, String value, int expireTime)
            throws Exception {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(DB_INDEX);
            jedis.set(key, value);
            if (expireTime > 0)
                jedis.expire(key, expireTime);
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            returnResource(jedis, isBroken);
        }
    }

    /**
     *  获取hash值
     */
    public List<String> getHmSet(String key){
        Jedis jedis = null;
//        Map<String, String> map;
        boolean isBroken = false;
        List<String> list;
        try {
            jedis = getJedis();
            jedis.select(DB_INDEX);
//            map = (Map<String, String>) jedis.hmget(key);
            list = jedis.hmget(key);
            return list;
        }catch (Exception e){
            isBroken = true;
            throw e;
        }finally {
            returnResource(jedis,isBroken);
        }

    }
    /**
     * 保存hash
     * @param key
     * @param map
     * @param expireTime
     */
    public void saveHmSet(String key, Map<String, String> map, int expireTime){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            jedis = getJedis();
            jedis.select(DB_INDEX);
            jedis.hmset(key, map);
            if(expireTime > 0) {
                jedis.expire(key, expireTime);
            }
        }catch (Exception e){
           isBroken = true;
            throw e;
        }

    }
	
	/**
	 * flush
	 */
	public void flushDB(){
		Jedis jedis = getJedis();
        boolean isBroken = false;
		try{
			jedis.flushDB();
		}catch(Exception e){
            isBroken = true;
		}finally{
			returnResource(jedis, isBroken);
		}
	}
	
	/**
	 * size
	 */
	public Long dbSize(){
		Long dbSize = 0L;
		Jedis jedis = getJedis();
        boolean isBroken = false;
		try{
			dbSize = jedis.dbSize();
		}catch(Exception e){
            isBroken = true;
		}finally{
			returnResource(jedis, isBroken);
		}
		return dbSize;
	}
    
    /**
	 * keys
	 * @param pattern
	 * @return
	 */
	public Set<byte[]> keys(String pattern){
		Set<byte[]> keys = null;
		Jedis jedis = getJedis();
        boolean isBroken = false;
		try{
			keys = jedis.keys(pattern.getBytes());
		}catch(Exception e){
            isBroken = true;
		}finally{
			returnResource(jedis, isBroken);
		}
		return keys;
	}

    /**
     * keys
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern, int dbIndex){
        Set<String> keys = null;
        Jedis jedis = getJedis();
        jedis.select(dbIndex);
        boolean isBroken = false;
        try{
            keys = jedis.keys(pattern);
        }catch(Exception e){
            isBroken = true;
        }finally{
            returnResource(jedis, isBroken);
        }
        return keys;
    }

    /**
     *
     * @param key
     * @param expireTime
     * @return
     */
    public long incr(String key, int expireTime){
        Jedis jedis = getJedis();
        jedis.select(DB_INDEX);
        long keyValue=0 ;
        boolean isBroken = false;
        try{
            keyValue = jedis.incr(key);
            if(expireTime > 0) {
                jedis.expire(key, expireTime);
            }
        }catch(Exception e){
            isBroken = true;
        }finally{
            returnResource(jedis, isBroken);
        }
        return keyValue;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
