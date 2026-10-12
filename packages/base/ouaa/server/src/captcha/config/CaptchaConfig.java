package com.guhecloud.cube.biz.ouaa.server.captcha.config;

import com.guhecloud.cube.biz.ouaa.server.captcha.properties.AjCaptchaProperties;
import com.guhecloud.cube.biz.ouaa.server.captcha.service.CaptchaCacheService;
import com.guhecloud.cube.biz.ouaa.server.captcha.service.impl.CaptchaCacheServiceRedisImpl;
import com.guhecloud.cube.biz.ouaa.server.captcha.service.impl.CaptchaServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class CaptchaConfig {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Bean(name = "AjCaptchaCacheService")
    @Primary
    public CaptchaCacheService captchaCacheService(AjCaptchaProperties config) {
        //缓存类型redis/local/....
        CaptchaCacheService ret = CaptchaServiceFactory.getCache(config.getCacheType().name());
        if (ret instanceof CaptchaCacheServiceRedisImpl) {
            ((CaptchaCacheServiceRedisImpl) ret).setStringRedisTemplate(redisTemplate);
        }
        return ret;
    }
}
