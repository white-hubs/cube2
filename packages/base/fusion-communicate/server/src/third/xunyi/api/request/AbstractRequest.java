package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.IProviderConfig;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.config.model.ProviderConfig;
import com.guhecloud.cube.biz.fusion.communicate.server.third.base.constants.FusionCommunicateConfigConstant;
import com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.config.XunYiConfig;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
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
import java.util.HashMap;
import java.util.Map;

@Data
@SuperBuilder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractRequest {

    /**
     * jsessionid
     */
    @JsonIgnore
    protected String token;

    @JsonIgnore
    public XunYiConfig getRequestConfig() {
        IProviderConfig providerConfig = ApplicationContextHolder.getBean(IProviderConfig.class);
        ProviderConfig config = providerConfig.getProviderConfig(FusionCommunicateConfigConstant.CONFIG_KEY);
        return JsonUtils.deserialize(config.getConfig(), XunYiConfig.class);
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
        return param;
    }

    @JsonIgnore
    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isNotBlank(getToken())) {
            headers.set("token", getToken());
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @JsonIgnore
    public HttpEntity<String> getHttpEntityForGet() {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpEntity<String> request = null;
        try {
            request = new HttpEntity<>(objectMapper.writeValueAsString(this), this.getHttpHeaders());
        } catch (JsonProcessingException e) {
            log.error("json_procession exception", e);
            e.printStackTrace();
        }
        return request;
    }

    @JsonIgnore
    public HttpEntity<AbstractRequest> getHttpEntity(AbstractRequest o) {
        return new HttpEntity<>(o, this.getHttpHeaders());
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
        private StringBuilder stringBuilder = new StringBuilder();
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
            XunYiConfig xunYiConfig = basicRequest.getRequestConfig();
            StringBuilder hostBuilder = new StringBuilder();
            if (!xunYiConfig.getServerApiAddr().startsWith(HTTPS_SCHEMA) &&
                !xunYiConfig.getServerApiAddr().startsWith(HTTP_SCHEMA)) {
                hostBuilder.append(HTTP_SCHEMA);
            }
            hostBuilder.append(xunYiConfig.getServerApiAddr());
            return hostBuilder.append(this.stringBuilder.toString().replace("//", SPLIT)).toString();
        }

        @Override
        public String toString() {
            return build();
        }
    }
}
