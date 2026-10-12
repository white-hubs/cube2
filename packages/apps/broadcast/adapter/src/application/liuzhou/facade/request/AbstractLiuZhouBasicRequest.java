package com.guhecloud.cube.biz.adapter.broadcast.server.application.liuzhou.facade.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Data
@SuperBuilder
@Slf4j
@AllArgsConstructor
public abstract class AbstractLiuZhouBasicRequest {

    @JsonIgnore
    public SubsystemAdapterConfig getRequestConfig() {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        //从当前请求头获取厂家信息
        return subsystemRedisUtils.getSubAdapterRefreshConfig();
    }

    @JsonIgnore
    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Keep-Alive", "timeout=30, max=1000");
        return headers;
    }

    @JsonIgnore
    public HttpEntity<Object> getHttpEntity(Object o) {
        return new HttpEntity<>(o, this.getHttpHeaders());
    }

    @JsonIgnore
    public abstract String getUrl();

    @JsonIgnore
    public RequestUrl getUrl(String path) {
        RequestUrl requestUrl = new RequestUrl(this);
        requestUrl.append(path);
        return requestUrl;
    }

    public static class RequestUrl {
        private final StringBuilder stringBuilder = new StringBuilder();
        private static final String SPLIT = "/";
        private static final String SCHEMA = "http://";
        private static final String HTTPS_SCHEMA = "https://";
        private final AbstractLiuZhouBasicRequest basicRequest;

        public RequestUrl(AbstractLiuZhouBasicRequest basicRequest) {
            this.basicRequest = basicRequest;
        }

        public RequestUrl append(String path) {
            stringBuilder.append(path);
            return this;
        }

        public String build() {
            SubsystemAdapterConfig subsystemConfig = basicRequest.getRequestConfig();
            StringBuilder hostBuilder = new StringBuilder();
            if (subsystemConfig.getServiceAddr().startsWith(HTTPS_SCHEMA) || subsystemConfig.getServiceAddr().startsWith(SCHEMA)) {
                hostBuilder.append(subsystemConfig.getServiceAddr());
            } else {
                hostBuilder.append(SCHEMA);
                hostBuilder.append(subsystemConfig.getServiceAddr());
            }
            hostBuilder.append(":").append(subsystemConfig.getServicePort());
            if (StringUtils.isNotEmpty(subsystemConfig.getServiceAddrSuffix())) {
                hostBuilder.append(SPLIT).append(subsystemConfig.getServiceAddrSuffix());
            }
            return hostBuilder.append(this.stringBuilder.toString().replace("//", SPLIT)).toString();
        }

        @Override
        public String toString() {
            return build();
        }
    }
}
