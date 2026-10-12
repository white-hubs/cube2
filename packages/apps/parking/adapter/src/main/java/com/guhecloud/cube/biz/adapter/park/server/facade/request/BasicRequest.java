package com.guhecloud.cube.biz.adapter.park.server.facade.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.biz.adapter.park.server.application.bosnia.entity.BosniaConfig;
import com.guhecloud.cube.biz.adapter.park.server.facade.model.Head;
import com.guhecloud.cube.biz.adapter.park.server.facade.model.HeadRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.BasicResponse;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public abstract class BasicRequest {

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
        return param;
    }

    @JsonIgnore
    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("tenant", "");
        return headers;
    }

    @JsonIgnore
    public HttpEntity getHttpEntity(String mcode) {
        return this.getHttpEntity(mcode, null);
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
    public HttpEntity getHttpEntityNoWithBody() {
        HttpEntity request = new HttpEntity(null, this.getHttpHeaders());
        return request;
    }

    @JsonIgnore
    public HttpEntity getHttpEntity(String mcode, Object o) {
        BosniaConfig bosniaConfig = JsonUtils.deserialize(JsonUtils.serialize(this.getRequestConfig()
                                                                                      .getManufacturerJson()), BosniaConfig.class);
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("head", Collections.singleton(getHeadBody(mcode, bosniaConfig)));
        if (Objects.nonNull(o)) {
            requestMap.put("body", Collections.singletonList(o));
        } else {
            requestMap.put("body", Collections.emptyList());
        }
        HttpEntity request = new HttpEntity(requestMap, this.getHttpHeaders());
        return request;
    }

    public <T> ParameterizedTypeReference<BasicResponse<T>> buildResponse(Class<T> clazz) {
        ParameterizedTypeImpl type = ParameterizedTypeImpl.make(BasicResponse.class, new Type[]{ clazz }
                , BasicResponse.class.getDeclaringClass());
        return ParameterizedTypeReference.forType(type);
    }

    @JsonIgnore
    public abstract String getUrl();

    public RequestUrl getUrl(String path) {
        RequestUrl requestUrl = new RequestUrl(this);
        requestUrl.append(path);
        return requestUrl;
    }

    private Head getHeadBody(String mcode, BosniaConfig bosniaConfig) {
        Head head = new HeadRequest(mcode, "0001", "10", "11", bosniaConfig.getAppSecret());
        return head;
    }

    public static class RequestUrl {
        private StringBuilder stringBuilder = new StringBuilder();
        private static final String SPLIT = "/";
        private static final String SCHEMA = "http://";
        private final BasicRequest basicRequest;

        public RequestUrl(BasicRequest basicRequest) {
            this.basicRequest = basicRequest;
        }

        public RequestUrl append(String path) {
            stringBuilder.append(SPLIT + path + SPLIT);
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
            hostBuilder.append(":" + subsystemConfig.getServicePort());
            if (StringUtils.isNotEmpty(subsystemConfig.getServiceAddrSuffix())) {
                hostBuilder.append("/" + subsystemConfig.getServiceAddrSuffix());
            }
            String s = hostBuilder.append(this.stringBuilder.toString().replaceAll("//", SPLIT)).toString();
            String url = s.substring(0, s.length() - 1);
            int indexOf = url.indexOf("?");
            if (indexOf != -1) {
                String substring = url.substring(indexOf, url.length());
                url = url.substring(0, indexOf - 1) + substring.replaceAll("/", "");
                return url;
            }
            return url;
        }

        @Override
        public String toString() {
            return build();
        }
    }
}
