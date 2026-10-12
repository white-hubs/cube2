package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.guhecloud.cube.biz.ouaa.server.service.VerificationCodeService;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author pengjian
 * @date 2022/10/9 14:27
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VerificationCodeServiceImpl implements VerificationCodeService {
    private final RedisTemplate redisTemplate;

    @Override
    public boolean verify(String inputCode, String key) {
        String code = (String) redisTemplate.opsForValue().get(PWD_CODE_KEY + key);
        if (StringUtils.isEmpty(code)) {
            return false;
        }

        redisTemplate.delete(PWD_CODE_KEY + key);
        if (code.equalsIgnoreCase(inputCode)) {
            return true;
        }

        return false;
    }
}
