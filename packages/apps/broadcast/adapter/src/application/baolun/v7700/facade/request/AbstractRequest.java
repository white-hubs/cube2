package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.facade.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.entity.BaoLunVSevenSevenConfig;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
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

@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractRequest extends BaseHttp {

    @JsonIgnore
    public BaoLunVSevenSevenConfig getRequestConfig() {
        SubsystemRedisUtils subsystemRedisUtils = ApplicationContextHolder.getBean(SubsystemRedisUtils.class);
        //从当前请求头获取厂家信息
        return JsonUtils.deserialize(subsystemRedisUtils.getSubAdapterRefreshConfig()
                                             .getManufacturerJson(), BaoLunVSevenSevenConfig.class);
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
            BaoLunVSevenSevenConfig baoLunVSevenSevenConfig = basicRequest.getRequestConfig();
            StringBuilder hostBuilder = new StringBuilder();
            if (!baoLunVSevenSevenConfig.getServerApiAddr().startsWith(HTTPS_SCHEMA) &&
                !baoLunVSevenSevenConfig.getServerApiAddr().startsWith(HTTP_SCHEMA)) {
                hostBuilder.append(HTTP_SCHEMA);
            }
            hostBuilder.append(baoLunVSevenSevenConfig.getServerApiAddr());
            return hostBuilder.append(this.stringBuilder.toString().replace("//", SPLIT)).toString();
        }

        @Override
        public String toString() {
            return build();
        }
    }
}
