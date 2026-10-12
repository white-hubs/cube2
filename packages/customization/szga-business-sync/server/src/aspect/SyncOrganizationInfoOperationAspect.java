package com.guhecloud.cube.srv.gen.business.info.sync.server.aspect;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.Organization;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncOrganizationService;
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
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/18 14:51
 * @Description: 组织信息更新切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncOrganizationInfoOperationAspect {
    private static final String WEB_HOOK_URL = "WEB_HOOK_URL";
    private static final String ORGANIZATION_SYNC_URL = "ORGANIZATION_SYNC_URL";
    private static final String URL = "url";
    private static final String TOKEN = "token";

    private final RedisTemplate<String, Object> redisTemplate;
    private final SyncOrganizationService syncOrganizationService;
    private final WebClient webClient;

    @Pointcut(
            "execution(* com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncOrganizationInfoOperationHandler.insert(..)) ||" +
            "execution(* com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncOrganizationInfoOperationHandler.update(..)) ||" +
            "execution(* com.guhecloud.cube.srv.gen.business.info.sync.server.handler.SyncOrganizationInfoOperationHandler.delete(..))"
    )
    public void point() {
        // This method is used to define the pointcut expression for info update
    }

    @AfterReturning(value = "point()", returning = "res")
    public void refreshAspect(JoinPoint joinPoint, Object res) {
        try {
            if (Objects.isNull(res)) {
                return;
            }

            List<Organization> organizationList = syncOrganizationService.lambdaQuery()
                    .like(Organization::getOrgPathId, res)
                    .list();
            if (CollectionUtils.isEmpty(organizationList)) {
                return;
            }
            DataDictionaryDTO dataDictionaryVO = this.get(WEB_HOOK_URL, ORGANIZATION_SYNC_URL, DataDictionaryDTO.class);
            if (Objects.isNull(dataDictionaryVO) || StringUtils.isBlank(dataDictionaryVO.getVariable())) {
                return;
            }
            Map<String, String> paramMap = JsonUtils.deserialize(dataDictionaryVO.getVariable(), new TypeReference<Map<String, String>>() {
            });
            log.info("webhook接口回调开始,接口:{}", paramMap.get(URL));
            for (Organization organization : organizationList) {
                //进行回调
                webClient.post()
                        .uri(paramMap.get(URL))
                        .header(HttpHeaders.CONTENT_TYPE, "application/json")
                        .header(HttpHeaders.AUTHORIZATION, paramMap.get(TOKEN))
                        .bodyValue(JsonUtils.serialize(organization))
                        .retrieve()
                        .onStatus(HttpStatusCode::isError,
                                  clientResponse -> Mono.error(new RuntimeException(
                                          "Failed : WebHook HTTP error code : " + clientResponse.statusCode()))
                                 )
                        .bodyToMono(String.class)
                        .subscribe(response -> {
                            log.info("Success to WebHook url" + paramMap.get(URL));
                        });
            }
        } catch (Exception e) {
            log.error("数据信息修改后回调失败,{}", e);
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
