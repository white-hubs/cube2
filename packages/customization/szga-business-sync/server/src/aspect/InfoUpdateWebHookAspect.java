package com.guhecloud.cube.srv.gen.business.info.sync.server.aspect;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.UserInfoVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.dto.DataDictionaryDTO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/1/20 18:18
 * @Description: 信息更新回调切面
 */

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InfoUpdateWebHookAspect {
    private static final String WEB_HOOK_URL = "WEB_HOOK_URL";
    private static final String BUSINESS_SYNC_URL = "BUSINESS_SYNC_URL";
    private static final String URL = "url";
    private static final String TOKEN = "token";
    /**
     * 判断是否由当前系统生成的id，如果是当前系统的后续会被刷新不进行回调
     */
    public static final ThreadLocal<Boolean> CREATE_USER = ThreadLocal.withInitial(() -> false);
    private final RedisTemplate<String, Object> redisTemplate;
    private final SyncUserService userService;
    private final WebClient webClient;

    @Pointcut("execution(* com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncUserInfoOperationHandler.increaseUser(..)) ||" +
              "execution(* com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncUserInfoOperationHandler.updateUser(..)) ||" +
              "execution(* com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncUserInfoOperationHandler.deleteUserMessage(..)) ||" +
              "execution(* com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncUserInfoOperationHandler.inductionComplete(..))"
    )
    public void point() {
        // This method is used to define the pointcut expression for info update
    }

    @AfterReturning(value = "point()", returning = "res")
    public void refreshAspect(JoinPoint joinPoint, Object res) {
        try {
            if (Objects.isNull(res) || CREATE_USER.get()) {
                return;
            }
            Long userId = Long.valueOf(res.toString());
            UserInfoVO userInfo = userService.getUserInfo(userId);
            if (Objects.isNull(userInfo)) {
                return;
            }
            DataDictionaryDTO dataDictionaryVO = this.get(WEB_HOOK_URL, BUSINESS_SYNC_URL, DataDictionaryDTO.class);
            if (Objects.isNull(dataDictionaryVO) || StringUtils.isBlank(dataDictionaryVO.getVariable())) {
                return;
            }
            Map<String, String> paramMap = JsonUtils.deserialize(dataDictionaryVO.getVariable(), new TypeReference<Map<String, String>>() {
            });
            //进行回调
            log.info("webhook接口回调开始,接口:{}", paramMap.get(URL));
            webClient.post()
                    .uri(paramMap.get(URL))
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .header(HttpHeaders.AUTHORIZATION, paramMap.get(TOKEN))
                    .bodyValue(JsonUtils.serialize(userInfo))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError,
                              clientResponse -> Mono.error(new RuntimeException(
                                      "Failed : WebHook HTTP error code : " + clientResponse.statusCode()))
                             )
                    .bodyToMono(String.class)
                    .subscribe(response -> {
                        log.info("Success to WebHook url" + paramMap.get(URL));
                    });
        } catch (Exception e) {
            log.error("数据信息修改后回调失败,{}", e);
        } finally {
            CREATE_USER.remove();
        }
    }

    public <T> T get(String itemCode, String code, Class<T> clazz) {
        Object value = this.redisTemplate.opsForValue().get(this.getRedisKey(itemCode, code));
        if (Objects.isNull(value)) {
            return null;
        }
        return JsonUtils.deserialize(value.toString(), clazz);
    }

    public String getRedisKey(String itemCode, String code) {
        return "DICTIONARY:" + itemCode + ":" + SessionContextHolder.getRequester().getTenant() + ":" + code;
    }
}
