package com.guhecloud.cube.iot.mgt.comm.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用 Java 21 实现的雪花ID (Snowflake ID) 生成器工具类。
 *
 * <p>此版本采用单例模式实现，确保在JVM中只有一个实例。
 * 它通过静态内部类的方式实现了线程安全的懒加载。
 *
 * <p><b>配置方式:</b>
 * ID的配置通过系统属性 (System Properties) 或 环境变量 (Environment Variables) 读取，优先级如下:
 * 1. 系统属性: -Dsnowflake.worker.id=1 -Dsnowflake.datacenter.id=1
 * 2. 环境变量: SNOWFLAKE_WORKER_ID=1 SNOWFLAKE_DATACENTER_ID=1
 * 3. 默认值: 如果两者都未配置，将使用默认值 0，并打印警告信息。
 *
 *  <p>雪花ID是一个64位的long类型整数，用于在分布式系统中生成唯一的ID。
 *  其结构如下:
 *  0 - 0000000000 0000000000 0000000000 0000000000 0 -    00000    -   00000   -  000000000000
 *  |   |                                             |             |           |              |
 *  |   |              41位时间戳 (毫秒)                | 5位数据中心ID | 5位机器ID  | 12位序列号   |
 *  |  1位未使用符号位
 *
 *  <p>这个实现是线程安全的。
 *  <p> 私有
 *
 */
@Slf4j
final class SnowflakeIdGenerator {

    // 起始时间戳 (纪元)，一旦定义，不应更改。
    // 这里设置为 2024-01-01T00:00:00Z
    private static final long EPOCH = 1704067200000L;

    // 机器ID所占的位数
    private static final long WORKER_ID_BITS = 5L;
    // 数据中心ID所占的位数
    private static final long DATACENTER_ID_BITS = 5L;
    // 序列号所占的位数
    private static final long SEQUENCE_BITS = 12L;

    // 支持的最大机器ID，结果是31
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    // 支持的最大数据中心ID，结果是31
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);

    // 机器ID向左移12位
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    // 数据中心ID向左移17位 (12+5)
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    // 时间戳向左移22位 (12+5+5)
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    // 生成序列的掩码，这里为4095 (0b111111111111=0xfff)
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private final long workerId;
    private final long datacenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 私有构造函数，防止外部直接实例化。
     */
    private SnowflakeIdGenerator() {
        this.datacenterId = getProperty("snowflake.datacenter.id", "SNOWFLAKE_DATACENTER_ID", 0);
        this.workerId = getProperty("snowflake.worker.id", "SNOWFLAKE_WORKER_ID", 0);

        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID can't be greater than " + MAX_WORKER_ID + " or less than 0");
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException("Datacenter ID can't be greater than " + MAX_DATACENTER_ID + " or less than 0");
        }
        log.info("SnowflakeIdGenerator initialized with datacenterId: {}, workerId: {}", datacenterId, workerId);
    }

    /**
     * 静态内部类，用于持有单例实例。
     */
    private static class SingletonHolder {
        private static final SnowflakeIdGenerator INSTANCE = new SnowflakeIdGenerator();
    }

    /**
     * 获取单例实例的静态方法。
     *
     * @return SnowflakeIdGenerator的唯一实例
     */
    public static SnowflakeIdGenerator getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static String nextIdStr() {
        return String.valueOf(getInstance().nextId());
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public long nextId() {
        lock.lock();
        try {
            long timestamp = timeGen();

            if (timestamp < lastTimestamp) {
                throw new RuntimeException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
            }

            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & SEQUENCE_MASK;
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }

            lastTimestamp = timestamp;

            return ((timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT)
                    | (datacenterId << DATACENTER_ID_SHIFT)
                    | (workerId << WORKER_ID_SHIFT)
                    | sequence;
        } finally {
            lock.unlock();
        }
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            Thread.onSpinWait(); // Java 9+ for busy-waiting
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return Instant.now().toEpochMilli();
    }

    /**
     * 从系统属性或环境变量中获取配置值。
     *
     * @param propertyName  系统属性名称
     * @param envName       环境变量名称
     * @param defaultValue  默认值
     * @return 配置值
     */
    private static long getProperty(String propertyName, String envName, long defaultValue) {
        String value = System.getProperty(propertyName);
        if (value == null) {
            value = System.getenv(envName);
        }

        if (value != null) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse property/env '" + propertyName + "', using default value '" + defaultValue + "'. Error: " + e.getMessage());
                return defaultValue;
            }
        }

        log.info("Warning: Property '{}' or environment variable '{}' not set. Using default value '{}'. This is not recommended for production.", propertyName, envName, defaultValue);
        return defaultValue;
    }


    // ============================== 使用示例 =================================
    public static void main(String[] args) {
        // 通过静态方法获取单例实例
        SnowflakeIdGenerator idGenerator = SnowflakeIdGenerator.getInstance();

        // 生成10个ID并打印
        System.out.println("生成10个雪花ID:");
        for (int i = 0; i < 10; i++) {
            long id = idGenerator.nextId();
            System.out.println(id);
        }

        // 演示高并发场景
        System.out.println("\n在多线程环境下生成ID:");
        try (var executor = java.util.concurrent.Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 20; i++) {
                executor.submit(() -> {
                    // 在不同线程中获取的仍然是同一个实例
                    long id = SnowflakeIdGenerator.getInstance().nextId();
                    System.out.println(Thread.currentThread().getName() + ": " + id);
                });
            }
        }
    }
}