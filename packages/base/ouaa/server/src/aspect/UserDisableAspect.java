package com.guhecloud.cube.biz.ouaa.server.aspect;

import com.guhecloud.cube.base.message.sdk.SiteMessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.request.SiteSendRequest;
import com.guhecloud.cube.biz.ouaa.server.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.server.security.invalidation.AccountTokenInvalidation;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Aspect
@Component
public class UserDisableAspect {

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private SiteMessageSendClient siteMessageSendClient;

    @Autowired
    private AccountTokenInvalidation accountTokenInvalidation;

    @Pointcut("execution(* com.guhecloud.cube.biz.ouaa.server.controller.tenant.UserUnderTenantController.updateByTenant(..))")
    public void point() {
        // This method is used to define the pointcut expression for updateByTenant method in UserUnderTenantController
    }

    @AfterReturning(value = "point()", returning = "res")
    public void refreshAspect(JoinPoint joinPoint, Object res) {
        try {
            Object[] args = joinPoint.getArgs();
            Long userId = Long.valueOf(args[0].toString());
            UserModifyParam userModifyParam = (UserModifyParam) args[1];
            if (!userModifyParam.getEnabled()) {
                //移除token
                this.accountTokenInvalidation.removeToken(userModifyParam.getUsername());
                //通知的终端
                Set<String> terminals = new HashSet<>(this.terminalService.getTenantTerminal());
                SiteSendRequest siteSendRequest = SiteSendRequest.builder()
                        .userIds(Collections.singleton(userId))
                        .eventType("NOTICE_MESSAGE")
                        .msgType("ACCOUNT_DISABLE_MESSAGE")//账号禁用消息
                        .content("ACCOUNT_DISABLE_MESSAGE")
                        .terminals(terminals)
                        .build();
                this.siteMessageSendClient.send(siteSendRequest);
            }
        } catch (Exception e) {
            log.error("用户禁用后置处理失败", e);
        }
    }
}
