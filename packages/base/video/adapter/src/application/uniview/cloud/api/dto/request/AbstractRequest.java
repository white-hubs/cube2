package com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.config.UniViewConfig;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractRequest extends BaseHttp {

    @JsonIgnore
    public UniViewConfig getRequestConfig() {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        SubsystemAdapterConfig refreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig();
        if (Objects.isNull(refreshConfig)) {
            log.warn("获取宇视api基础配置为空");
            return null;
        }
        return JsonUtils.deserialize(refreshConfig.getManufacturerJson(), UniViewConfig.class);
    }

    /**
     * 获取单层对象的字段值
     * 如果复杂对象或者多级对象，请使用post body传参
     * 只用于get简单传参
     *
     * @return
     */
    @JsonIgnore
    public Map<String, Object> getParamForGet() {
        Map<String, Object> param = new HashMap<>(2);
        //获取所有字段以及字段名称以及值
        Class<? extends AbstractRequest> clazz = this.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        if (declaredFields.length != 0) {
            Arrays.stream(declaredFields).forEach(field -> {
                field.setAccessible(true);
                String fieldName = field.getName();
                try {
                    Object o = field.get(this);
                    param.put(fieldName, o);
                } catch (IllegalAccessException e) {
                    log.error("反射违法访问异常：", e);
                }
            });
        }
        return param;
    }

    @JsonIgnore
    public HttpHeaders getHttpHeaders() {
        return this.getCustomHttpHeaders();
    }

    @JsonIgnore
    protected abstract HttpHeaders getCustomHttpHeaders();

    @JsonIgnore
    public HttpEntity<AbstractRequest> getHttpEntity(AbstractRequest o) {
        return new HttpEntity<>(o, this.getHttpHeaders());
    }

    @JsonIgnore
    public abstract String getRequestUrl();

    @JsonIgnore
    public RequestUrl getRequestUrl(String path) {
        RequestUrl requestUrl = new RequestUrl(this);
        requestUrl.append(path);
        return requestUrl;
    }

    public static class RequestUrl {
        private final StringBuilder stringBuilder = new StringBuilder();
        private static final String SPLIT = "/";
        private static final String HTTP_SCHEMA = "http://";
        private static final String HTTPS_SCHEMA = "https://";
        private final AbstractRequest basicRequest;

        public RequestUrl(AbstractRequest basicRequest) {
            this.basicRequest = basicRequest;
        }

        public RequestUrl append(String path) {
            stringBuilder.append(path);
            return this;
        }

        public String build() {
            UniViewConfig uniViewConfig = basicRequest.getRequestConfig();
            StringBuilder hostBuilder = new StringBuilder();
            if (!uniViewConfig.getSchema().startsWith(HTTPS_SCHEMA) &&
                !uniViewConfig.getSchema().startsWith(HTTP_SCHEMA)) {
                hostBuilder.append(HTTP_SCHEMA);
            } else {
                hostBuilder.append(uniViewConfig.getSchema());
            }
            hostBuilder.append(uniViewConfig.getHost());
            return hostBuilder.append(this.stringBuilder.toString().replace("//", SPLIT)).toString();
        }

        @Override
        public String toString() {
            return build();
        }
    }
}
