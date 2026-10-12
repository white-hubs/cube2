package com.guhecloud.cube.base.message2.server.handler.handler.send.wecom;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.handler.domain.wecom.WeComAccountConfig;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.BaseMsgSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.wecom.dto.WeComAccessTokenResponseDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.wecom.dto.WeComMsgSendResponseDTO;
import com.guhecloud.cube.base.message2.server.handler.handler.send.wecom.dto.WeComUserIdResponse;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WeComSender extends BaseMsgSender {

    private final RedisTemplate<String, String> redisTemplate;

    private final RestTemplate restTemplate;

    private static final String WE_COM_ACCESS_TOKEN_CACHE_KEY = "weComMsgAccessToken";

    private static final String WE_COM_USER_CACHE_KEY = "weComUser";

    /**
     * 内置线程池，后续用户可以指定自己的线程池
     */
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            // 核心线程数
            2,
            //maximumPoolSize: 池中允许的最大的线程数
            5,
            //keepAliveTime: 当线程数大于核心线程数的时候，线程在最大多长时间没有接到新任务就会终止释放， 最终线程池维持在 corePoolSize 大小
            50,
            //时间单位
            TimeUnit.SECONDS,
            //workQueue: 阻塞队列，用来存储等待执行的任务，如果当前对线程的需求超过了 corePoolSize 大小，就会放在这里等待空闲线程执行。
            new LinkedBlockingQueue<>(101),
            //threadFactory: 创建线程的工厂，比如指定线程名等
            new ThreadFactoryBuilder().setNameFormat("weComPhoneConvertUser-pool-%d").build(),
            //拒绝策略，如果线程满了，线程池就会拒绝,被拒绝的任务的处理程序，该处理程序以静默方式丢弃
            new ThreadPoolExecutor.DiscardPolicy()
    );

    public WeComSender(RedisTemplate<String, String> redisTemplate, RestTemplate restTemplate) {
        this.redisTemplate = redisTemplate;
        this.restTemplate = restTemplate;
        this.channelCode = ChannelEnum.CHANNEL_WE_COM;
    }

    @Override
    public MessageSendResponse doSend(MessageInfo messageInfo) {
        ResponseEntity<WeComMsgSendResponseDTO> weComAppPushResponse;
        try {
            var weComAccountConfig = WeComAccountConfig.getWeComConfig(messageInfo.getSendAccountInfo());
            Map<String, Object> params = new HashMap<>();

            params.put("touser", String.join("|", this.convertWeComUserId(messageInfo.getReceiver(),
                                                                          getAccessToken(weComAccountConfig, messageInfo.getConfigTenant()),
                                                                          messageInfo.getConfigTenant())));
            params.put("msgtype", "text");
            params.put("agentid", weComAccountConfig.getAppAgentId());
            Map<String, Object> content = new HashMap<>();
            content.put("content", messageInfo.getContent());
            params.put("text", content);
            params.put("safe", 0);
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            weComAppPushResponse = this.restTemplate.exchange(
                    "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" +
                    getAccessToken(weComAccountConfig, messageInfo.getConfigTenant()), HttpMethod.POST, new HttpEntity<>(params, headers), WeComMsgSendResponseDTO.class);
            if (weComAppPushResponse.getStatusCode() == HttpStatus.OK &&
                0 == Objects.requireNonNull(weComAppPushResponse.getBody()).getErrcode()) {
                return MessageSendResponse
                        .builder()
                        .id(messageInfo.getId())
                        .succeedReceiver(messageInfo.getReceiver())
                        .succeed(true)
                        .build();
            }
            throw new ServiceException(JsonUtils.serialize(weComAppPushResponse.getBody()));
        } catch (Exception e) {
            log.error("消息发送任务id为{}企业微信发送失败", messageInfo.getId(), e);
            return MessageSendResponse
                    .builder()
                    .id(messageInfo.getId())
                    .succeed(false)
                    .message(e.getMessage())
                    .failReceivers(messageInfo.getReceiver().stream().map(phone -> new MessageSendResponse.Receiver(phone, e.getMessage()))
                                           .collect(Collectors.toList()))
                    .build();
        }
    }

    private String getAccessToken(WeComAccountConfig weComAccountConfig, Integer tenant) {
        var accessToken = this.redisTemplate.opsForValue()
                .get(WE_COM_ACCESS_TOKEN_CACHE_KEY + ":" + tenant);
        if (StringUtils.isEmpty(accessToken)) {
            var accessTokenResponse = this.login(weComAccountConfig);
            this.redisTemplate.opsForValue()
                    .set(WE_COM_ACCESS_TOKEN_CACHE_KEY + ":" + tenant, accessTokenResponse.getAccess_token(),
                         accessTokenResponse.getExpires_in() / 2, TimeUnit.SECONDS);
            return accessTokenResponse.getAccess_token();
        }
        return accessToken;
    }

    private WeComAccessTokenResponseDTO login(WeComAccountConfig weComAccountConfig) {
        //构建请求体
        var httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(httpHeaders);
        var loginBuilder = "https://qyapi.weixin.qq.com/cgi-bin/gettoken" +
                           "?corpid=" +
                           weComAccountConfig.getCorpId() +
                           "&corpsecret=" +
                           weComAccountConfig.getAppSecret();
        var accessTokenResponse = this.restTemplate.exchange(loginBuilder, HttpMethod.GET, entity, WeComAccessTokenResponseDTO.class);
        log.info("企业微信获取access_token返回结果:{}", accessTokenResponse);
        if (accessTokenResponse.getStatusCode() == HttpStatus.OK &&
            0 == Objects.requireNonNull(accessTokenResponse.getBody()).getErrcode()) {
            return accessTokenResponse.getBody();
        }
        return new WeComAccessTokenResponseDTO();
    }

    @Override
    public String verification(MessageInfo messageInfo) {
        return StringUtils.EMPTY_STRING;
    }

    /**
     * 转换手机号到企业微信的userid
     *
     * @param phones
     * @param tenant
     *
     * @return
     */
    public List<String> convertWeComUserId(Set<String> phones, String accessToken, Integer tenant) {
        var key = WE_COM_USER_CACHE_KEY + ":" + tenant;
        var hashOperation = this.redisTemplate.opsForHash();
        var hasKeys = hashOperation.keys(key);
        if (CollectionUtils.isEmpty(hasKeys)) {
            var weComUserIdByPhones = getWeComUserIdByPhones(phones, accessToken, tenant);
            this.redisTemplate.opsForHash().putAll(key, weComUserIdByPhones);
            return new ArrayList<>(weComUserIdByPhones.values());
        } else {
            var hasPhones = hasKeys.stream().map(Object::toString).collect(Collectors.toSet());
            var foundKeys = hasKeys.stream().filter(foundKey -> phones.contains(foundKey.toString())).collect(Collectors.toSet());
            var notFoundPhones = phones.stream().filter(phone -> !hasPhones.contains(phone)).collect(Collectors.toSet());
            var userIds = this.redisTemplate.opsForHash().multiGet(key, foundKeys).stream()
                    .map(Object::toString).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(notFoundPhones)) {
                var weComUserIdByPhones = getWeComUserIdByPhones(notFoundPhones, accessToken, tenant);
                this.redisTemplate.opsForHash().putAll(key, weComUserIdByPhones);
                userIds.addAll(new ArrayList<>(weComUserIdByPhones.values()));
            }
            return userIds;
        }
    }

    public Map<String, String> getWeComUserIdByPhones(Set<String> phones, String accessToken, Integer tenant) {
        List<Future<Map<String, String>>> futures = new ArrayList<>();
        phones.forEach(phone -> {
            futures.add(executor.submit(new PhoneConvertWeComUserIdRunnable(phone, accessToken, this.restTemplate)));
        });
        Map<String, String> allMap = new HashMap<>();
        futures.forEach(future -> {
            try {
                var phoneUserIdMap = future.get(5, TimeUnit.SECONDS);
                if (MapUtils.isNotEmpty(phoneUserIdMap)) {
                    allMap.putAll(phoneUserIdMap);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                log.error("手机号转企业微信userId失败", e);
            }
        });
        return allMap;
    }

    public static class PhoneConvertWeComUserIdRunnable implements Callable<Map<String, String>> {

        private final String phone;

        private final String accessToken;

        private final RestTemplate restTemplate;

        public PhoneConvertWeComUserIdRunnable(String phone, String accessToken, RestTemplate restTemplate) {
            this.phone = phone;
            this.accessToken = accessToken;
            this.restTemplate = restTemplate;
        }

        @Override
        public Map<String, String> call() throws Exception {
            //构建请求体
            Map<String, Object> params = new HashMap<>();
            params.put("mobile", phone);
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            var loginBuilder = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserid?access_token=" + accessToken;
            var userIdResponseResponse = restTemplate.exchange(loginBuilder, HttpMethod.POST, new HttpEntity<>(params, headers), WeComUserIdResponse.class);
            log.info("企业微信获取用户id返回结果:{}", userIdResponseResponse);
            if (userIdResponseResponse.getStatusCode() == HttpStatus.OK &&
                0 == Objects.requireNonNull(userIdResponseResponse.getBody()).getErrcode()) {
                Map<String, String> map = new HashMap<>();
                map.put(phone, userIdResponseResponse.getBody().getUserid());
                return map;
            }
            return null;
        }
    }
}
