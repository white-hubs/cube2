package com.guhecloud.platform.cloud.logger.config;

import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.mzt.logapi.beans.Operator;
import com.mzt.logapi.service.IOperatorGetService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration("BizLogRecordConfiguration")
public class BizLogRecordConfiguration {
    @Bean
    public IOperatorGetService operatorGetService() {
        return () -> Optional.of(SecurityContextHolder.getPrincipal().getName())
                .map(a -> {
                    String userInfo =
                            SecurityContextHolder.getPrincipal().getAccount() + "_" + SecurityContextHolder.getPrincipal().getName();
                    return new Operator(userInfo);
                }).orElse(null);
    }
}
