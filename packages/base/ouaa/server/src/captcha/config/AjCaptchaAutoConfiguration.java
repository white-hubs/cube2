package com.guhecloud.cube.biz.ouaa.server.captcha.config;

import com.guhecloud.cube.biz.ouaa.server.captcha.properties.AjCaptchaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(AjCaptchaProperties.class)
@Import({ AjCaptchaServiceAutoConfiguration.class, AjCaptchaStorageAutoConfiguration.class })
public class AjCaptchaAutoConfiguration {
}
