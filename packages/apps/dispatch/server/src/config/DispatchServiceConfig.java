package com.guhecloud.cube.biz.dispatch.server.config;

import com.guhecloud.cube.biz.dispatch.server.service.DispatchService;
import com.guhecloud.cube.biz.dispatch.server.service.impl.PTDispatchServiceImpl;
import com.guhecloud.cube.biz.dispatch.server.service.impl.XYDispatchServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置第三方调度台的实现
 */
@Configuration
public class DispatchServiceConfig {

    @Value("${dispatch.platform:PT}")
    private String dispatchPlatform;

    //    @Bean
    //    public DispatchService dispatchService(){
    //        return new PTDispatchServiceImpl();
    //  }

    @Bean
    public DispatchService dispatchService() {
        if (dispatchPlatform.equals("XY")) {
            return new XYDispatchServiceImpl();
        } else {
            return new PTDispatchServiceImpl();
        }
    }
}
