package com.guhecloud.platform.redis.util;

import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonRedLock;
import org.redisson.api.RFencedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.api.redisnode.RedisClusterMaster;
import org.redisson.api.redisnode.RedisClusterSlave;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.config.Config;
import org.redisson.redisnode.RedissonClusterNodes;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import com.guhecloud.platform.base.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Redis工具类
 * 废弃的方法需要做实现替换
 *
 * @author Woody
 * @date 2021/6/30
 */
@Slf4j
@Component
@ConditionalOnClass(RedisTemplate.class)
public class RedisUtils implements ApplicationContextAware {
    /**
     * 获取锁重试次数
     */
    private static final int LOCKING_RETRY_COUNT = 3;

    /**
     * 序列号Rdids Key前缀
     */
    private static final String SEQUENCE_REDIS_PREFIX = "sequence:";

    /**
     * 数字签名Rdids Key前缀
     */
    private static final String SIGNATURE_REDIS_PREFIX = "signature:";

    /**
     * Redis解锁脚本（原子操作）
     */
    private static final String UNLOCK_SCRIPT =
            "if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end";

    /**
     * Redis连接工厂
     */
    private static RedisConnectionFactory CONNECTION_FACTORY;

    /**
     * 获取Redis连接工厂对象
     *
     * @return Redis连接工厂对象
     */
    public static RedisConnectionFactory getConnectionFactory() {
        return Objects.requireNonNull(CONNECTION_FACTORY, "Redis connection factory has not been initialized");
    }

    /**
     * 执行Redis操作
     *
     * @param function 操作方法
     * @param <T>      返回数据类型泛型
     *
     * @return 执行结果
     */
    public static <T> T execute(Function<RedisConnection, T> function) {
        return execute(function, e -> {
            log.warn("Redis execute failed: {}", e.getMessage());
            return null;
        });
    }

    /**
     * 执行Redis操作
     *
     * @param function 操作方法
     * @param failure  操作失败回调方法
     * @param <T>      返回数据类型泛型
     *
     * @return 执行结果
     */
    public static <T> T execute(@NonNull Function<RedisConnection, T> function,
                                @NonNull Function<Exception, T> failure
                               ) {
        RedisConnection connection = null;
        RedisConnectionFactory factory = getConnectionFactory();
        try {
            connection = factory.getConnection();
            return function.apply(connection);
        } catch (Exception e) {
            return failure.apply(e);
        } finally {
            if (connection != null) {
                RedisConnectionUtils.releaseConnection(connection, factory);
            }
        }
    }

    public static DistributedLock getDistributedLock() {
        return new DistributedLock();
    }

    public static class DistributedLock {

        /**
         * 红锁对象已弃用。更多详情请参阅此文
         * https://martin.kleppmann.com/2016/02/08/how-to-do-distributed-locking.html
         *
         * @param name
         * @param expiration
         *
         * @return
         */
        @Deprecated
        public RedissonRedLock tryLock(@NonNull String name, @NonNull Expiration expiration) {
            RedissonClient redissonClient = ApplicationContextHolder.getBean(RedissonClient.class);
            //获取redission配置
            Config config = redissonClient.getConfig();
            int needLockNumbers = 1;
            //集群环境RedLock所需锁数量等于所有主节点+所有从节点，保证这些锁至少落到 节点数/2+1的节点上
            if (config.isClusterConfig()) {
                RedissonClusterNodes redissonClusterNodes = (RedissonClusterNodes) redissonClient.getRedisNodes(RedisNodes.CLUSTER);
                Collection<RedisClusterMaster> redisClusterMasters = redissonClusterNodes.getMasters();
                Collection<RedisClusterSlave> redisClusterNodesSlaves = redissonClusterNodes.getSlaves();
                needLockNumbers = redisClusterMasters.size() + redisClusterNodesSlaves.size();
            }
            int lockIndex = 0;
            RLock[] rLocks = new RLock[needLockNumbers];
            do {
                rLocks[lockIndex] = redissonClient.getLock(name + (lockIndex + 1));
                lockIndex++;
            } while (lockIndex < needLockNumbers);
            RedissonRedLock rLock = new RedissonRedLock(rLocks);
            try {
                boolean isLocked = rLock.tryLock(expiration.getExpirationTime(), expiration.getTimeUnit());
                if (isLocked) {
                    return rLock;
                }
            } catch (Exception e) {
                log.warn("Redis locking failed: {}", e.getMessage());
                rLock.unlock();
            }
            return null;
        }

        /**
         * 获取fencedLock
         * 推荐使用
         *
         * @param name
         * @param expiration
         *
         * @return
         */
        public Record tryFencedLock(@NonNull String name, @NonNull Expiration expiration) {
            var redissonClient = ApplicationContextHolder.getBean(RedissonClient.class);
            var fencedLock = redissonClient.getFencedLock(name);
            try {
                var token = fencedLock.tryLockAndGetToken(expiration.getExpirationTime(), expiration.getTimeUnit());
                if (token != -1) { // -1表示获取锁失败
                    return new FencedLockResult(fencedLock, token);
                }
            } catch (Exception e) {
                log.warn("redis fenced lock fail:{}", e.getMessage());
                fencedLock.unlock();
            }
            return null;
        }
    }

    public record FencedLockResult(RFencedLock lock, long fencingToken) {
    }

    /**
     * 尝试加锁（无阻塞），如果加锁成功则返回锁标识，否则返回null
     *
     * @param name       锁名称
     * @param expiration 锁超时时间（毫秒）
     *
     * @return 锁标识
     */
    public static String tryLock(String name, long expiration) {
        Assert.isTrue(expiration > 0, "expiration must be greater than 0");
        return tryLock(name, Expiration.from(expiration, TimeUnit.MILLISECONDS));
    }

    /**
     * 尝试加锁（无阻塞），如果加锁成功则返回锁标识，否则返回null
     *
     * @param name       锁名称
     * @param expiration 锁有效期
     *
     * @return 锁标识
     */
    public static String tryLock(@NonNull String name, @NonNull Expiration expiration) {
        String token = UUID.randomUUID().toString();
        byte[] key = name.getBytes(StandardCharsets.UTF_8);
        byte[] value = token.getBytes(StandardCharsets.UTF_8);
        return execute(connection -> {
            Boolean success = connection.set(key, value, expiration, RedisStringCommands.SetOption.SET_IF_ABSENT);
            return success ? token : null;
        }, e -> {
            log.warn("Redis locking failed: {}", e.getMessage());
            return null;
        });
    }

    /**
     * 加锁（阻塞），加锁成功则返回锁标识
     *
     * @param name 锁名称
     *
     * @return 锁标识
     */
    public static String lock(String name) {
        return lock(name, 10 * 60 * 1000);
    }

    /**
     * 加锁（阻塞），加锁成功则返回锁标识
     *
     * @param name       锁名称
     * @param expiration 锁超时时间（毫秒）
     *
     * @return 锁标识
     */
    public static String lock(String name, long expiration) {
        Assert.isTrue(expiration > 0, "expiration must be greater than 0");
        return lock(name, Expiration.from(expiration, TimeUnit.MILLISECONDS));
    }

    /**
     * 加锁（阻塞），加锁成功则返回锁标识
     *
     * @param name       锁名称
     * @param expiration 锁有效期
     *
     * @return 锁标识
     */
    public static String lock(String name, Expiration expiration) {
        int count = 1;
        String token;
        do {
            if (StringUtils.isEmpty(token = tryLock(name, expiration))) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        } while (count++ < LOCKING_RETRY_COUNT && StringUtils.isEmpty(token) && !Thread.interrupted());
        return token;
    }

    /**
     * 解锁
     *
     * @param name  锁名称
     * @param token 锁标识
     *
     * @return true/false
     */
    public static boolean unlock(@NonNull String name, @NonNull String token) {
        byte[] script = UNLOCK_SCRIPT.getBytes(StandardCharsets.UTF_8);
        byte[][] args = new byte[][]{ name.getBytes(StandardCharsets.UTF_8), token.getBytes(StandardCharsets.UTF_8) };
        return execute(connection -> {
            Long result = connection.scriptingCommands().eval(script, ReturnType.INTEGER, 1, args);
            return result != null && result > 0;
        }, e -> {
            log.warn("Redis unlock failed: {}", e.getMessage());
            return false;
        });
    }

    /**
     * 加/解锁环绕执行方法
     *
     * @param name     锁名称
     * @param consumer 回调方法
     */
    public static void lockAround(String name, @NonNull Consumer<String> consumer) {
        lockAround(name, t -> {
            consumer.accept(t);
            return null;
        });
    }

    /**
     * 加/解锁环绕执行方法
     *
     * @param name     锁名称
     * @param function 回调方法
     * @param <T>      返回数据类型泛型
     *
     * @return 执行结果
     */
    public static <T> T lockAround(String name, Function<String, T> function) {
        return lockAround(name, function, () -> null);
    }

    /**
     * 加/解锁环绕执行方法
     *
     * @param name     锁名称
     * @param function 回调方法
     * @param failure  枷锁失败回调方法
     * @param <T>      返回数据类型泛型
     *
     * @return 执行结果
     */
    public static <T> T lockAround(String name, @NonNull Function<String, T> function, @NonNull Supplier<T> failure) {
        String token = lock(name);
        if (StringUtils.isEmpty(token)) {
            return failure.get();
        }
        try {
            return function.apply(token);
        } finally {
            unlock(name, token);
        }
    }

    /**
     * 生成唯一标识，默认1分钟有效期
     *
     * @return 唯一标识
     */
    public static long generateIdentity() {
        return generateIdentity(Expiration.from(60, TimeUnit.SECONDS));
    }

    /**
     * 生成唯一标识
     * <p>
     * 唯一标识格式：yyyyMMddHHmmss + 四位随机数，例如：202108251933453756
     *
     * @param expiration 唯一标识有效期
     *
     * @return 唯一标识
     */
    public static long generateIdentity(@NonNull Expiration expiration) {
        for (int i = 0; i < 10; i++) {
            long sequence = ObjectUtils.generateIdentity();
            byte[] key = (SEQUENCE_REDIS_PREFIX + sequence).getBytes(StandardCharsets.UTF_8);
            Boolean successful = execute(connection -> connection.set(key, ObjectUtils.EMPTY_BYTE_ARRAY, expiration,
                                                                      RedisStringCommands.SetOption.SET_IF_ABSENT));
            if (Boolean.TRUE.equals(successful)) {
                return sequence;
            }
        }
        throw new RuntimeException("Generate sequence attempts limit exceeded");
    }

    /**
     * 数字签名，默认有效时间3分钟
     *
     * @param parameters 签名参数
     *
     * @return 签名信息
     */
    public static String sign(Map<?, ?> parameters) {
        return sign(parameters, Expiration.from(3, TimeUnit.MINUTES));
    }

    /**
     * 数字签名
     *
     * @param parameters 签名参数
     * @param expiration 有效时间
     *
     * @return 签名信息
     */
    public static String sign(Map<?, ?> parameters, @NonNull Expiration expiration) {
        String signature = SecurityContextHolder.sign(parameters);
        byte[] key = (SIGNATURE_REDIS_PREFIX + signature).getBytes(StandardCharsets.UTF_8);
        Boolean successful = execute(connection -> connection.set(key, ObjectUtils.EMPTY_BYTE_ARRAY,
                                                                  expiration, RedisStringCommands.SetOption.SET_IF_ABSENT));
        if (!Boolean.TRUE.equals(successful)) {
            throw new RuntimeException("Sign failed");
        }
        return signature;
    }

    /**
     * 验证数字签名
     *
     * @param signature  数字签名信息
     * @param parameters 签名参数
     *
     * @return true/false
     */
    public static boolean validateSignature(String signature, Map<?, ?> parameters) {
        if (SecurityContextHolder.validateSignature(signature, parameters)) {
            byte[] key = (SIGNATURE_REDIS_PREFIX + signature).getBytes(StandardCharsets.UTF_8);
            Long result = execute(connection -> connection.del(key));
            return result != null && result > 0;
        }
        return false;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        CONNECTION_FACTORY = applicationContext.getBean(RedisConnectionFactory.class);
    }

}
