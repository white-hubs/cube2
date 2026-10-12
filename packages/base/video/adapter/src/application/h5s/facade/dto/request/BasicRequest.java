package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.config.H5sClientHttpConfig;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
@SuperBuilder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public abstract class BasicRequest {

    protected String session;

    public SubsystemAdapterConfig getRequestConfig() {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        //从当前请求头获取厂家信息
        return subsystemRedisUtils.getSubAdapterRefreshConfig();
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
        Class<? extends BasicRequest> clazz = this.getClass();
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
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    @JsonIgnore
    public HttpEntity getHttpEntityForGet() {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpEntity request = null;
        try {
            request = new HttpEntity(objectMapper.writeValueAsString(this), this.getHttpHeaders());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return request;
    }

    @JsonIgnore
    public HttpEntity getHttpEntity(Object o) {
        HttpEntity request = new HttpEntity(o, this.getHttpHeaders());
        return request;
    }

    @JsonIgnore
    public HttpEntity getHttpEntityNoWithBody() {
        HttpEntity request = new HttpEntity(null, this.getHttpHeaders());
        return request;
    }

    @JsonIgnore
    public abstract String getUrl();

    public RequestUrl getUrl(String path) {
        RequestUrl requestUrl = new RequestUrl(this);
        requestUrl.append(path);
        return requestUrl;
    }

    public static class RequestUrl {
        private StringBuilder stringBuilder = new StringBuilder();
        private static final String SPLIT = "/";
        private final BasicRequest basicRequest;

        public RequestUrl(BasicRequest basicRequest) {
            this.basicRequest = basicRequest;
        }

        public RequestUrl append(String path) {
            stringBuilder.append(path);
            return this;
        }

        public String build() {
            H5sClientHttpConfig h5sClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(this.basicRequest
                                                                                                        .getRequestConfig()
                                                                                                        .getManufacturerJson()), H5sClientHttpConfig.class);
            StringBuilder hostBuilder = new StringBuilder();
            hostBuilder.append(h5sClientHttpConfig.getSchema());
            hostBuilder.append(h5sClientHttpConfig.getHost());
            hostBuilder.append(":" + h5sClientHttpConfig.getPort());
            hostBuilder.append(stringBuilder.toString());
            return hostBuilder.toString();
        }

        @Override
        public String toString() {
            return build();
        }
    }
}
