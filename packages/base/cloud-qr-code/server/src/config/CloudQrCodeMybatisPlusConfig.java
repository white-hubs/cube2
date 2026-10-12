package com.guhecloud.cube.base.cloud.qr.code.server.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.interceptor.ClientIsolateHandler;
import com.guhecloud.platform.database.interceptor.CustomTenantLineInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 二维码云化服务 MyBatis-Plus 配置
 * 配置客户端数据隔离拦截器
 *
 * @author system
 */
@Configuration
public class CloudQrCodeMybatisPlusConfig {

    /**
     * 配置客户端数据隔离拦截器
     * 自动为SQL添加 grant_client_id 过滤条件
     */
    @Bean
    public MybatisPlusInterceptor cloudQrCodeInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 添加客户端隔离拦截器
        interceptor.addInnerInterceptor(new CustomTenantLineInnerInterceptor() {
            {
                this.setTenantLineHandler(new ClientIsolateHandler());
            }
        });

        return interceptor;
    }
}
