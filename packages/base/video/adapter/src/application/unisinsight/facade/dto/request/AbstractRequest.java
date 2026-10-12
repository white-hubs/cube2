package com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.facade.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.unisinsight.config.UnisInsightHttpConfig;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Objects;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractRequest extends BaseHttpRequest {

    @JsonIgnore
    public UnisInsightHttpConfig getRequestConfig() {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);

        SubsystemAdapterConfig refreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig();
        if (Objects.isNull(refreshConfig)) {
            log.warn("获取紫光华智智能视觉管理平台基础配置为空");
            return null;
        }
        return JsonUtils.deserialize(refreshConfig.getManufacturerJson(), UnisInsightHttpConfig.class);
    }

    @JsonIgnore
    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        this.addCustomHttpHeaders(headers);
        return headers;
    }

    @JsonIgnore
    public abstract void addCustomHttpHeaders(HttpHeaders headers);

    @JsonIgnore
    public HttpEntity<AbstractRequest> getHttpEntity() {
        return new HttpEntity<>(null, this.getHttpHeaders());
    }

    @JsonIgnore
    public abstract String getUrl();

    public RequestUrl getUrl(String path) {
        RequestUrl requestUrl = new RequestUrl(this);
        requestUrl.append(path);
        return requestUrl;
    }

    public static class RequestUrl {

        private final StringBuilder stringBuilder = new StringBuilder();

        private final AbstractRequest basicRequest;

        public RequestUrl(AbstractRequest basicRequest) {
            this.basicRequest = basicRequest;
        }

        public RequestUrl append(String path) {
            stringBuilder.append(path);
            return this;
        }

        public String build() {
            UnisInsightHttpConfig requestConfig = this.basicRequest.getRequestConfig();
            String schema = requestConfig.getSchema();
            if (!schema.startsWith("http://") && !schema.startsWith("https://")) {
                schema = schema + "://";
            }
            StringBuilder hostBuilder = new StringBuilder(schema).append(requestConfig.getHost());
            if (Objects.nonNull(requestConfig.getPort()) && !requestConfig.getPort().isEmpty()) {
                hostBuilder.append(":").append(requestConfig.getPort());
            }
            return hostBuilder.append(stringBuilder).toString();
        }

        @Override
        public String toString() {
            return build();
        }
    }
}
