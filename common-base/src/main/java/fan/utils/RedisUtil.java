package fan.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author Fan
 * @since 2022/11/25 23:02
 */
@Component
public class RedisUtil {

    private static RedisTemplate<String, Object> redisTemplate;

    @Resource
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**
     * 设置缓存失效时间
     *
     * @param key  Key
     * @param time 过期时间, 单位秒
     * @return {@link boolean}
     * @author Fan
     * @since 2022/11/25 23:05
     */
    public static boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            LogUtil.error("过期时间设置失败" + e.getMessage());
            return false;
        }
    }

    /**
     * 批量删除缓存
     *
     * @param keys Keys
     * @author Fan
     * @since 2022/12/15 23:47
     */
    public static void del(String... keys) {
        if (CommonUtil.isNotBlank(keys)) {
            redisTemplate.delete(Arrays.asList(keys));
        }
    }

    /**
     * 批量删除缓存
     *
     * @param keys Keys
     * @author Fan
     * @since 2022/11/26 3:00
     */
    public static void del(List<String> keys) {
        if (CommonUtil.isNotBlank(keys)) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 判断 Key 是否存在
     *
     * @param key Key
     * @return {@link boolean}
     * @author Fan
     * @since 2022/11/27 2:49
     */
    public static boolean hasKey(String key) {
        try {
            return CommonUtil.isNotBlank(key) ? redisTemplate.hasKey(key) : false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * String类型 存储数据
     *
     * @param key   Key
     * @param value Value
     * @return {@link boolean}
     * @author Fan
     * @since 2022/11/25 23:10
     */
    public static boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            LogUtil.error("String类型 缓存存储失败" + e.getMessage());
            return false;
        }
    }

    /**
     * String类型 存储数据带过期时间
     *
     * @param key   Key
     * @param value Value
     * @param time  过期时间, 单位秒
     * @return {@link boolean}
     * @author Fan
     * @since 2022/11/25 23:13
     */
    public static boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                return true;
            } else {
                LogUtil.error("过期时间小于0");
                return false;
            }
        } catch (Exception e) {
            LogUtil.error("String类型 过期缓存存储失败" + e.getMessage());
            return false;
        }
    }

    /**
     * String类型  获取数据
     *
     * @param key Key
     * @return {@link Object}
     * @author Fan
     * @since 2022/11/25 23:08
     */
    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Hash类型 存储数据
     *
     * @param key     Key
     * @param hashKey HashKey
     * @param value   Value
     * @return {@link boolean}
     * @author Fan
     * @since 2022/11/25 23:21
     */
    public static boolean hashSet(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            LogUtil.error("Hash类型 缓存存储失败" + e.getMessage());
            return false;
        }
    }

    /**
     * Hash类型 存储数据带过期时间
     *
     * @param key     Key
     * @param hashKey HashKey
     * @param value   Value
     * @param time    过期时间, 单位秒
     * @return {@link boolean}
     * @author Fan
     * @since 2022/11/25 23:23
     */
    public static boolean hashSet(String key, String hashKey, Object value, long time) {
        try {
            hashSet(key, hashKey, value);

            if (time > 0) {
                expire(key, time);
                return true;
            } else {
                LogUtil.error("过期时间小于0");
                return false;
            }
        } catch (Exception e) {
            LogUtil.error("Hash类型 过期缓存存储失败" + e.getMessage());
            return false;
        }
    }

    /**
     * Hash类型 获取数据
     *
     * @param key     Key
     * @param hashKey HashKey
     * @return {@link Object}
     * @author Fan
     * @since 2022/11/25 23:25
     */
    public static Object hashGet(String key, String hashKey) {
        if (CommonUtil.isBlank(key) || CommonUtil.isBlank(hashKey)) {
            return null;
        }

        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * Hash类型 删除缓存, 支持批量
     *
     * @param key     Key
     * @param hashKey HashKey
     * @author Fan
     * @since 2022/11/25 23:43
     */
    public static void hashDel(String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }
}
