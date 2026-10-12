package com.guhecloud.cube.base.iot.product.model.v3.util;

import lombok.extern.slf4j.Slf4j;

/**
 * ID编码工具类
 *
 * 提供统一的ID生成和编码方案:
 * 1. 使用Twitter Snowflake算法生成分布式唯一ID (64位Long)
 * 2. 将Long型ID转换为Base62短码 (字符集: 0-9a-zA-Z)
 * 3. 短码长度约11-12位,适合作为业务编号(产品编号、版本号等)
 *
 * 优势:
 * - 全局唯一: Snowflake保证分布式环境下ID不重复
 * - 短小精悍: Base62编码后长度仅12位左右,易于阅读和传输
 * - 趋势递增: 包含时间戳,天然有序,利于索引
 * - 高性能: 本地生成,无需访问数据库
 */
@Slf4j
public final class IdCodec {

    private IdCodec() {
        throw new UnsupportedOperationException("工具类不允许实例化");
    }

    /** Base62字符集 (0-9a-zA-Z) */
    private static final char[] BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final int BASE = 62;

    /** Snowflake ID生成器单例 */
    private static final SnowflakeIdGenerator ID_GENERATOR = new SnowflakeIdGenerator(1L, 1L);

    /**
     * 生成短码(12位Base62编码)
     *
     * 用途: 产品编号、版本号等业务ID
     * 示例: "2Nq5zB7xK9m"
     *
     * @return 12位短码字符串
     */
    public static String generateShortCode() {
        long id = ID_GENERATOR.nextId();
        return encodeBase62(id);
    }

    /**
     * 将Long型ID编码为Base62字符串
     *
     * @param id Long型ID
     * @return Base62编码字符串
     */
    public static String encodeBase62(long id) {
        if (id == 0) {
            return String.valueOf(BASE62_CHARS[0]);
        }

        // 处理负数(虽然Snowflake ID始终为正数)
        boolean isNegative = id < 0;
        id = Math.abs(id);

        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            int remainder = (int) (id % BASE);
            sb.append(BASE62_CHARS[remainder]);
            id = id / BASE;
        }

        if (isNegative) {
            sb.append('-');
        }

        return sb.reverse().toString();
    }

    /**
     * 将Base62字符串解码为Long型ID
     *
     * @param encoded Base62编码字符串
     * @return Long型ID
     * @throws IllegalArgumentException 如果编码字符串无效
     */
    public static long decodeBase62(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            throw new IllegalArgumentException("编码字符串不能为空");
        }

        boolean isNegative = encoded.charAt(0) == '-';
        String str = isNegative ? encoded.substring(1) : encoded;

        long result = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            int digit = getBase62Value(c);
            if (digit < 0) {
                throw new IllegalArgumentException("非法Base62字符: " + c);
            }
            result = result * BASE + digit;
        }

        return isNegative ? -result : result;
    }

    /**
     * 获取Base62字符对应的数值
     *
     * @param c 字符
     * @return 对应数值,如果非法则返回-1
     */
    private static int getBase62Value(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'z') {
            return c - 'a' + 10;
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 36;
        }
        return -1;
    }

    /**
     * Snowflake ID生成器
     *
     * Twitter Snowflake算法:
     * 64位ID = 1位符号位(0) + 41位时间戳 + 10位机器ID + 12位序列号
     *
     * 理论上QPS: 4096 * 1000 = 409万/秒
     */
    private static class SnowflakeIdGenerator {
        /** 起始时间戳 (2024-01-01 00:00:00) */
        private static final long EPOCH = 1704038400000L;

        /** 机器ID位数 */
        private static final long WORKER_ID_BITS = 5L;
        /** 数据中心ID位数 */
        private static final long DATACENTER_ID_BITS = 5L;
        /** 序列号位数 */
        private static final long SEQUENCE_BITS = 12L;

        /** 机器ID最大值 */
        private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
        /** 数据中心ID最大值 */
        private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
        /** 序列号掩码 */
        private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

        /** 机器ID左移位数 */
        private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
        /** 数据中心ID左移位数 */
        private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
        /** 时间戳左移位数 */
        private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

        private final long workerId;
        private final long datacenterId;
        private long sequence = 0L;
        private long lastTimestamp = -1L;

        /**
         * 构造函数
         *
         * @param workerId 机器ID (0-31)
         * @param datacenterId 数据中心ID (0-31)
         */
        public SnowflakeIdGenerator(long workerId, long datacenterId) {
            if (workerId > MAX_WORKER_ID || workerId < 0) {
                throw new IllegalArgumentException(
                    String.format("Worker ID 必须在 0 到 %d 之间", MAX_WORKER_ID));
            }
            if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
                throw new IllegalArgumentException(
                    String.format("Datacenter ID 必须在 0 到 %d 之间", MAX_DATACENTER_ID));
            }
            this.workerId = workerId;
            this.datacenterId = datacenterId;
        }

        /**
         * 生成下一个ID
         *
         * @return 64位Long型ID
         */
        public synchronized long nextId() {
            long timestamp = currentTimeMillis();

            // 时钟回拨检测
            if (timestamp < lastTimestamp) {
                long offset = lastTimestamp - timestamp;
                if (offset <= 5) {
                    // 小于5ms,等待时钟追上
                    try {
                        wait(offset << 1);
                        timestamp = currentTimeMillis();
                        if (timestamp < lastTimestamp) {
                            throw new RuntimeException("时钟回拨异常");
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("ID生成被中断", e);
                    }
                } else {
                    throw new RuntimeException(
                        String.format("时钟回拨 %d 毫秒,拒绝生成ID", offset));
                }
            }

            // 同一毫秒内,序列号递增
            if (timestamp == lastTimestamp) {
                sequence = (sequence + 1) & SEQUENCE_MASK;
                if (sequence == 0) {
                    // 序列号溢出,等待下一毫秒
                    timestamp = waitNextMillis(lastTimestamp);
                }
            } else {
                // 不同毫秒,序列号归零
                sequence = 0L;
            }

            lastTimestamp = timestamp;

            // 组装ID: 时间戳 | 数据中心ID | 机器ID | 序列号
            return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
        }

        /**
         * 等待到下一毫秒
         */
        private long waitNextMillis(long lastTimestamp) {
            long timestamp = currentTimeMillis();
            while (timestamp <= lastTimestamp) {
                timestamp = currentTimeMillis();
            }
            return timestamp;
        }

        /**
         * 获取当前时间戳(毫秒)
         */
        private long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    }
}
