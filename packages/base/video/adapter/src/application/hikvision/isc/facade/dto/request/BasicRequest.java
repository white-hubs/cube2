package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.config.IscClientHttpConfig;
import com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response.BasicResponse;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class BasicRequest {

    //设置OpenAPI接口的上下文
    private String ARTEMIS_PATH = "artemis";

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
    public String getContentType() {
        return MediaType.APPLICATION_JSON_VALUE;
    }

    @JsonIgnore
    public String getHttpEntity(Object o) {
        return JsonUtils.serialize(o);
    }

    public <T> ParameterizedTypeReference<BasicResponse<T>> buildResponse(Class<T> clazz) {
        ParameterizedTypeImpl type = ParameterizedTypeImpl.make(BasicResponse.class, new Type[]{ clazz }
                , BasicResponse.class.getDeclaringClass());
        return ParameterizedTypeReference.forType(type);
    }

    @JsonIgnore
    public abstract Map<String, String> getUrl();

    public RequestUrl getUrl(String path) {
        RequestUrl requestUrl = new RequestUrl(this);
        requestUrl.append(path);
        return requestUrl;
    }

    public void setAuthInfo() {
        IscClientHttpConfig iscClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(this.getRequestConfig()
                                                                                                    .getManufacturerJson()), IscClientHttpConfig.class);
        /**
         * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
         */
        String host = iscClientHttpConfig.getHost();
        if (StringUtils.isNotEmpty(iscClientHttpConfig.getPort())) {
            host += ":" + iscClientHttpConfig.getPort();
        }
        ArtemisConfig.host = host; // 平台的ip端口
        ArtemisConfig.appKey = iscClientHttpConfig.getAppKey();  // 密钥appkey
        ArtemisConfig.appSecret = iscClientHttpConfig.getAppSecret();// 密钥appSecret
    }

    public static class RequestUrl {
        private StringBuilder stringBuilder = new StringBuilder();
        private static final String SPLIT = "/";
        private final BasicRequest basicRequest;

        public RequestUrl(BasicRequest basicRequest) {
            this.basicRequest = basicRequest;
        }

        public RequestUrl append(String path) {
            stringBuilder.append(SPLIT + basicRequest.ARTEMIS_PATH + path);
            return this;
        }

        public Map<String, String> build() {
            basicRequest.setAuthInfo();
            IscClientHttpConfig iscClientHttpConfig = JsonUtils.deserialize(JsonUtils.serialize(this.basicRequest
                                                                                                        .getRequestConfig()
                                                                                                        .getManufacturerJson()), IscClientHttpConfig.class);
            Map<String, String> param = new HashMap<>();
            param.put(iscClientHttpConfig.getSchema(), stringBuilder.toString());//根据现场环境部署确认是http还是https
            return param;
        }
    }
}
