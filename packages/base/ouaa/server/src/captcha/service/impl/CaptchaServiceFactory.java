package com.guhecloud.cube.biz.ouaa.server.captcha.service.impl;

import com.guhecloud.cube.biz.ouaa.server.captcha.model.common.Const;
import com.guhecloud.cube.biz.ouaa.server.captcha.service.CaptchaCacheService;
import com.guhecloud.cube.biz.ouaa.server.captcha.service.CaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

/**
 * @author ht
 */
public class CaptchaServiceFactory {

    private static Logger logger = LoggerFactory.getLogger(CaptchaServiceFactory.class);

    public static CaptchaService getInstance(Properties config) {
        String captchaType = config.getProperty(Const.CAPTCHA_TYPE, "default");
        CaptchaService ret = instances.get(captchaType);
        if (ret == null) {
            throw new RuntimeException("unsupported-[captcha.type]=" + captchaType);
        }
        ret.init(config);
        return ret;
    }

    public static CaptchaCacheService getCache(String cacheType) {
        return cacheService.get(cacheType);
    }

    public volatile static Map<String, CaptchaService> instances = new HashMap();
    public volatile static Map<String, CaptchaCacheService> cacheService = new HashMap();

    static {
        ServiceLoader<CaptchaCacheService> cacheServices = ServiceLoader.load(CaptchaCacheService.class);
        for (CaptchaCacheService item : cacheServices) {
            cacheService.put(item.type(), item);
        }
        logger.info("supported-captchaCache-service:{}", cacheService.keySet());
        ServiceLoader<CaptchaService> services = ServiceLoader.load(CaptchaService.class);
        for (CaptchaService item : services) {
            instances.put(item.captchaType(), item);
        }
        logger.info("supported-captchaTypes-service:{}", instances.keySet());
    }
}
