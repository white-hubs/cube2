package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.config.GhyHttpConfig;
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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractRequest extends BaseHttpRequest {

    public GhyHttpConfig getRequestConfig() {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        SubsystemAdapterConfig refreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig();
        if (Objects.isNull(refreshConfig)) {
            log.warn("获取古河云视频流媒体平台基础配置为空");
        }
        return JsonUtils.deserialize(refreshConfig.getManufacturerJson(), GhyHttpConfig.class);
    }

    /**
     * 获取单层对象的字段值
     * 如果复杂对象或者多级对象，请使用post body传参
     * 只用于get简单传参
     *
     * @return
     */
    @JsonIgnore
    public Map<String, ?> getParamForGet() {
        Map<String, Object> param = new HashMap<>(2);
        /**
         * 获取所有字段以及字段名称以及值
         *
         *
         */
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
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            Field[] superclassDeclaredFields = superclass.getDeclaredFields();
            Arrays.stream(superclassDeclaredFields).forEach(field -> {
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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        this.addCustomHttpHeaders(headers);
        return headers;
    }

    @JsonIgnore
    public abstract void addCustomHttpHeaders(HttpHeaders headers);

    @JsonIgnore
    public HttpEntity<AbstractRequest> getHttpEntity(AbstractRequest o) {
        return new HttpEntity<>(o, this.getHttpHeaders());
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
        private static final String SPLIT = "/";
        private final AbstractRequest basicRequest;

        public RequestUrl(AbstractRequest basicRequest) {
            this.basicRequest = basicRequest;
        }

        public RequestUrl append(String path) {
            stringBuilder.append(path);
            return this;
        }

        public String build() {
            GhyHttpConfig requestConfig = this.basicRequest
                    .getRequestConfig();
            return requestConfig.getSchema() +
                   requestConfig.getHost() +
                   ":" + requestConfig.getPort() +
                   stringBuilder;
        }

        @Override
        public String toString() {
            return build();
        }
    }
}
