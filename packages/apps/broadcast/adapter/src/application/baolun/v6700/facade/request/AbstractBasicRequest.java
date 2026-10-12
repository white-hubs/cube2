package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Data
@SuperBuilder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractBasicRequest {

    /**
     * jsessionid
     */
    @JsonIgnore
    protected String jsessionid;

    @JsonIgnore
    public SubsystemAdapterConfig getRequestConfig() {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        //从当前请求头获取厂家信息
        return subsystemRedisUtils.getSubAdapterRefreshConfig();
    }

    @JsonIgnore
    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isNotBlank(jsessionid)) {
            headers.set(HttpHeaders.COOKIE, "JSESSIONID=" + jsessionid);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        private final AbstractBasicRequest basicRequest;

        public RequestUrl(AbstractBasicRequest basicRequest) {
            this.basicRequest = basicRequest;
        }

        public RequestUrl append(String path) {
            stringBuilder.append(SPLIT).append(path).append(SPLIT);
            return this;
        }

        public String build() {
            SubsystemAdapterConfig subsystemConfig = basicRequest.getRequestConfig();
            StringBuilder hostBuilder = new StringBuilder();
            if (subsystemConfig.getServiceAddr().startsWith("https://") || subsystemConfig.getServiceAddr().startsWith("http://")) {
                hostBuilder.append(subsystemConfig.getServiceAddr());
            } else {
                hostBuilder.append(SCHEMA);
                hostBuilder.append(subsystemConfig.getServiceAddr());
            }
            hostBuilder.append(":").append(subsystemConfig.getServicePort());
            if (StringUtils.isNotEmpty(subsystemConfig.getServiceAddrSuffix())) {
                hostBuilder.append("/").append(subsystemConfig.getServiceAddrSuffix());
            }
            String s = hostBuilder.append(this.stringBuilder.toString().replace("//", SPLIT)).toString();
            String url = s.substring(0, s.length() - 1);
            if (StringUtils.isNotBlank(this.basicRequest.jsessionid)) {
                //拼接上jsessionid
                url = url + ";JSESSIONID=" + this.basicRequest.jsessionid;
            }
            return url;
        }

        @Override
        public String toString() {
            return build();
        }
    }
}
