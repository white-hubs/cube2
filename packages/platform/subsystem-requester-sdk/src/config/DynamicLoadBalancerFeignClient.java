package com.guhecloud.subsystem.requester.sdk.config;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.listener.nacos.enumeration.EndpointType;
import com.guhecloud.subsystem.config.listener.web.http.SubsystemHttpConstants;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.cloud.openfeign.loadbalancer.LoadBalancerFeignRequestTransformer;
import org.springframework.jmx.access.InvalidInvocationException;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DynamicLoadBalancerFeignClient extends FeignBlockingLoadBalancerClient {

    private final Client delegate;
    private final SubsystemRedisUtils subsystemRedisUtils;

    public DynamicLoadBalancerFeignClient(Client delegate, LoadBalancerClient loadBalancerClient,
                                          LoadBalancerClientFactory loadBalancerClientFactory,
                                          List<LoadBalancerFeignRequestTransformer> transformers,
                                          SubsystemRedisUtils subsystemRedisUtils
                                         ) {
        super(delegate, loadBalancerClient, loadBalancerClientFactory, transformers);
        this.subsystemRedisUtils = subsystemRedisUtils;
        this.delegate = delegate;
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        var asUri = URI.create(request.url());
        var clientName = asUri.getHost();
        //获取配置信息
        var subsystemConfig = subsystemRedisUtils.getSubEndpointRefreshConfig(
                clientName + ":" + SessionContextHolder.getRequester().getTenant().toString());
        if (Objects.isNull(subsystemConfig)) {
            throw new InvalidInvocationException("不存在路由配置信息");
        }
        //增加自定义请求头
        //创建一个新的Header map,request.headers得到的是一个只读的Map直接put会导致异常
        var requestHeader = new HashMap<>(request.headers());
        requestHeader.put(SubsystemHttpConstants.HEADER_SUBSYSTEM, Collections.singleton(clientName));
        requestHeader.put(SubsystemHttpConstants.HEADER_MANUFACTURER, Collections.singleton(subsystemConfig.getManufacturer()));
        //如果配置的是服务名
        if (EndpointType.SERVICE_NAME == subsystemConfig.getEndpointType()) {
            var replaceServer = subsystemConfig.getServiceName();
            if (StringUtils.isNotEmpty(subsystemConfig.getServiceAddrSuffix())) {
                replaceServer += "/" + subsystemConfig.getServiceAddrSuffix();
            }
            //重新构造一个新的request
            var newRequest = Request.create(request.httpMethod(), request.url()
                    .replace(clientName, replaceServer), requestHeader, request.body(), request.charset(), request.requestTemplate());
            return super.execute(newRequest, options);
        }
        //如果配置的是ip地址
        if (EndpointType.IP_ADDRESS == subsystemConfig.getEndpointType()) {
            var ipAddress = subsystemConfig.getServiceAddr();
            var port = subsystemConfig.getServicePort();
            var url = request.url();
            var scheme = asUri.getScheme();
            if (ipAddress.startsWith("https://")) {
                url = url.replace(scheme, "https:");
            } else if (ipAddress.startsWith("http")) {
                url = url.replace(scheme, "http");
            }
            var replaceServer = ipAddress + ":" + port;
            if (StringUtils.isNotEmpty(subsystemConfig.getServiceAddrSuffix())) {
                replaceServer += "/" + subsystemConfig.getServiceAddrSuffix();
            }
            //重新构造一个新的request
            var newRequest = Request.create(request.httpMethod(), url.replace(clientName, replaceServer), requestHeader, request.body(), request.charset(), request.requestTemplate());
            return this.delegate.execute(newRequest, options);
        }
        throw new InvalidInvocationException("不存在路由配置信息");
    }
}
