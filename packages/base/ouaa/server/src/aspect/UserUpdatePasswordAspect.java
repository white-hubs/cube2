package com.guhecloud.cube.biz.ouaa.server.aspect;

import com.guhecloud.cube.base.message.sdk.SiteMessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.request.SiteSendRequest;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.security.invalidation.AccountTokenInvalidation;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.service.UserService;
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
public class UserUpdatePasswordAspect {

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private SiteMessageSendClient siteMessageSendClient;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountTokenInvalidation accountTokenInvalidation;

    @Pointcut("execution(* com.guhecloud.cube.biz.ouaa.server.controller.context.ContextController.updatePassword(..)) ||" +
              "execution(* com.guhecloud.cube.biz.ouaa.server.controller.context.ContextController.resetPassword(..))"
    )
    public void point() {
        // This method is used to define the pointcut expression for updateByTenant method in ContextController
    }

    @AfterReturning(value = "point()", returning = "res")
    public void refreshAspect(JoinPoint joinPoint, Object res) {
        try {
            Object[] args = joinPoint.getArgs();
            Long userId = Long.valueOf(args[0].toString());
            User user = this.userService.getById(userId);
            //移除token
            this.accountTokenInvalidation.removeToken(user.getUsername());
            //通知的终端
            Set<String> terminals = new HashSet<>(this.terminalService.getTenantTerminal());
            SiteSendRequest siteSendRequest = SiteSendRequest.builder()
                    .userIds(Collections.singleton(userId))
                    .eventType("NOTICE_MESSAGE")
                    .msgType("ACCOUNT_PASSWORD_UPDATE")//账号禁用消息
                    .content("ACCOUNT_PASSWORD_UPDATE")
                    .terminals(terminals)
                    .build();
            this.siteMessageSendClient.send(siteSendRequest);
        } catch (Exception e) {
            log.error("用户密码修改后置处理失败", e);
        }
    }
}
