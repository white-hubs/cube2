package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.generator;

import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.utils.NumberSystemConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Slf4j
@Service
public class ShortCodeService {

    private final StringRedisTemplate redisTemplate;
    private static final String QR_CODE_SET_KEY = "cloud:qr:code:set";
    private static final int SHORT_CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    public ShortCodeService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 生成唯一短码，使用Redis Set去重
     */
    public String getNextShortCode() {
        for (int attempt = 0; attempt < 100; attempt++) {
            // 生成6位随机62进制短码
            String shortCode = generateRandomShortCode();
            
            // 使用Redis SADD原子操作检查并添加，返回添加的元素个数
            Long added = redisTemplate.opsForSet().add(QR_CODE_SET_KEY, shortCode);
            
            if (added != null && added > 0) {
                log.debug("生成唯一短码: {}", shortCode);
                return shortCode;
            }
            
            log.warn("短码冲突: {}, 重试第{}次", shortCode, attempt + 1);
        }
        
        // 100次均失败，降级使用UUID前6位
        String fallback = UUID.randomUUID().toString().replace("-", "").substring(0, SHORT_CODE_LENGTH);
        redisTemplate.opsForSet().add(QR_CODE_SET_KEY, fallback);
        log.error("短码生成100次均冲突，使用降级方案: {}", fallback);
        return fallback;
    }

    /**
     * 生成随机6位62进制短码
     */
    private String generateRandomShortCode() {
        // 生成随机数（62^6 = 56,800,235,584）
        long randomValue = Math.abs(RANDOM.nextLong()) % (long) Math.pow(62, SHORT_CODE_LENGTH);
        String shortCode = NumberSystemConverter.toBase62(randomValue);
        
        // 补齐6位
        if (shortCode.length() < SHORT_CODE_LENGTH) {
            shortCode = "0".repeat(SHORT_CODE_LENGTH - shortCode.length()) + shortCode;
        }
        
        return shortCode;
    }
}
