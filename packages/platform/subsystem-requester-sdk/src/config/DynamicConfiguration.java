package com.guhecloud.subsystem.requester.sdk.config;

import com.guhecloud.platform.client.CustomFeignErrorDecoder;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import feign.Client;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.LoadBalancerFeignRequestTransformer;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 自定义动态路由配置
 *
 * @author lanchun
 * @date 2022/9/19 15:10
 */
public class DynamicConfiguration {

    @Bean
    public Client feignClient(LoadBalancerClient loadBalancerClient,
                              LoadBalancerClientFactory loadBalancerClientFactory,
                              List<LoadBalancerFeignRequestTransformer> transformers,
                              SubsystemRedisUtils subsystemRedisUtils
                             ) {
        return new DynamicLoadBalancerFeignClient(new Client.Default(null, null),
                                                  loadBalancerClient,
                                                  loadBalancerClientFactory,
                                                  transformers,
                                                  subsystemRedisUtils);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }
}
