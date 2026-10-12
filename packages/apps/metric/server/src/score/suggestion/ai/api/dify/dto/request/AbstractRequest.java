package com.guhecloud.cube.biz.metric.server.score.suggestion.ai.api.dify.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.biz.metric.server.score.suggestion.ai.api.dify.config.DifyChatConfig;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
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
public abstract class AbstractRequest extends BaseHttp {

    private static final String SUB_SYSTEM_CODE = "THIRD_SERVICE";

    private static final String KEY = "SAFETY_REPORT::AI::CHAT::CONFIG";

    @JsonIgnore
    public DifyChatConfig getRequestConfig() {
        DataDictionaryAdapter dataDictionaryAdapter = ApplicationContextHolder.getBean(DataDictionaryAdapter.class);
        DataDictionaryaramByItemAndCodeParam param = new DataDictionaryaramByItemAndCodeParam();
        param.setItemCode(SUB_SYSTEM_CODE);
        param.setCode(KEY);
        DataDictionaryDTO dictionary = dataDictionaryAdapter.getDataDictionaryByItemAndCode(param);
        if (Objects.isNull(dictionary) || StringUtils.isBlank(dictionary.getVariable())) {
            log.warn("dify chat config is null or empty");
            return null;
        }
        return JsonUtils.deserialize(dictionary.getVariable(), DifyChatConfig.class);
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getRequestConfig().getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @JsonIgnore
    public HttpEntity<AbstractRequest> getHttpEntity(AbstractRequest o) {
        return new HttpEntity<>(o, this.getHttpHeaders());
    }

    @JsonIgnore
    public abstract String getRequestUrl();

    @JsonIgnore
    public RequestUrl getRequestUrl(String path) {
        RequestUrl requestUrl = new RequestUrl();
        requestUrl.append(path);
        return requestUrl;
    }

    public static class RequestUrl {
        private final StringBuilder stringBuilder = new StringBuilder();

        public RequestUrl() {
        }

        public RequestUrl append(String path) {
            stringBuilder.append(path);
            return this;
        }

        public String build() {
            return this.stringBuilder.toString();
        }

        @Override
        public String toString() {
            return build();
        }
    }
}
