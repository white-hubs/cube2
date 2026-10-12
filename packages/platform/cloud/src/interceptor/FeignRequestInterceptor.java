package com.guhecloud.platform.cloud.interceptor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.AESUtils;
import com.guhecloud.platform.security.config.FeignSecurityProperties;
import feign.RequestTemplate;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.util.Map;

public class FeignRequestInterceptor extends OAuth2FeignRequestInterceptor {

    private final FeignSecurityProperties feignSecurityProperties;

    private static final String FEIGN_INNER_ORIGINAL = "FEIGN_INNER_ORIGINAL";

    public FeignRequestInterceptor(FeignSecurityProperties feignSecurityProperties,
                                   OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager,
                                   OAuth2ClientProperties oAuth2ClientProperties
                                  ) {
        super(oAuth2AuthorizedClientManager, oAuth2ClientProperties);
        this.feignSecurityProperties = feignSecurityProperties;
    }

    @Override
    public void apply(RequestTemplate template) {
        // 复制当前所有请求头信息
        // 1. 如果当前线程由Http请求触发则会自动构建Requester并设置到上下文
        // 2. 如果当前线程由Cron或外部任务调度触发，则需要通过显式设置请求租户等方法产生Requester并设置到上下文
        // 3. 如果当前线程由MQ消息触发，则会自动从消息中解析出Requester并设置到上下文
        // 注意此处统一从上下文中提取Requester，可以覆盖以上多种情况；不要直接从HttpServletRequest中复制请求头，这样只能处理第1中情况
        // 2、3情况可能出现上下文中无Requester的情况，需要判空，例如业务代码没有显式设置请求租户、mq消息不是内部业务发出的
        var requester = SessionContextHolder.getRequester(true);
        if (requester != null) {
            Map<String, String> requesterMap = JSON.parseObject(JSON.toJSONString(requester), new TypeReference<Map<String, String>>() {
            });
            for (String key : requesterMap.keySet()) {
                template.header(key, requesterMap.get(key));
            }
        }
        //内部请求请求头中放入签名信息(内部服务调用,不再进行资源等权限控制)
        //生成方式:
        //1.密钥+时间戳
        if (!template.headers().containsKey("feign_inner_invoke")) {
            template.header("feign_inner_invoke", AESUtils.encrypt(feignSecurityProperties.getSecretKey(),
                                                                   FEIGN_INNER_ORIGINAL + "@" + System.currentTimeMillis() +
                                                                   feignSecurityProperties.getTimeout() * 1000));
        }
        // 调用父类OAuth2FeignRequestInterceptor的apply方法设置认证头Authorization（实际为Oauth的client_credentials认证方式）
        // 第一次会发起额外的一次http请求获取token，之后会存储于OAuth2ClientContext避免每次都要获取
        if (requester == null || StringUtils.isEmpty(requester.getAuthorization())) {
            super.apply(template);
        }
    }
}
