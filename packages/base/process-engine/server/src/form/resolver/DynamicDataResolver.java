package com.guhecloud.cube.base.flow.form.resolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.base.flow.form.enumeration.FormJsonEnum;
import com.guhecloud.cube.base.flow.form.util.HttpClientUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.Base64Utils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bq
 * @date 2022/10/20
 * @description 表单控件动态数据源解析器
 */
@Slf4j
@Component
public class DynamicDataResolver {

    public Object parse(@NonNull String originalData) {
        try {
            JsonNode jsonNode = JsonUtils.deserialize(originalData);
            Map<String, Object> schema = JsonUtils.deserialize(jsonNode, FormJsonEnum.JSON_KEY_DYNAMIC_DATA.getValue(), new TypeReference<>() {});
            Object json = this.parseDynamicData(schema);
            return json;
        } catch (Exception e) {
            log.error("获取动态数据源数据失败");
            throw new ServiceException("获取动态数据失败，请检查配置");
        }
    }

    private Object parseDynamicData(Map<String, Object> dynamicData) {
        if (null == dynamicData) {
            return null;
        }
        String url = dynamicData.get(FormJsonEnum.JSON_KEY_URL.getValue()).toString();
        String method = dynamicData.get(FormJsonEnum.JSON_KEY_METHOD.getValue()).toString();
        Map params = (Map) dynamicData.get(FormJsonEnum.JSON_KEY_PARAMS.getValue());
        // 拼装query params的参数
        MultiValueMap<String, String> multiParamMap = new LinkedMultiValueMap<>();
        if (params != null) {
            params.forEach((k, v) -> {
                multiParamMap.add((String) k, (String) v);
            });
        }
        // 接口认证,如果是内部接口则无需拼接认证参数，外部接口才需拼接认证参数
        Map<String, String> authMap = new HashMap<>();
        Boolean inside = (Boolean) dynamicData.get(FormJsonEnum.JSON_KEY_INSIDE.getValue());
        if (null != inside && inside) {
            // 内部系统
            url = SessionContextHolder.getRequester().getDomain() + url;
            authMap.put(FormJsonEnum.HEADER_AUTHORIZATION.getValue(), SessionContextHolder.getRequester().getAuthorization());
            authMap.put(FormJsonEnum.HEADER_TENANT.getValue(), SessionContextHolder.getRequester().getTenant().toString());
            authMap.put(FormJsonEnum.HEADER_TERMINAL.getValue(), SessionContextHolder.getRequester().getTerminal());
        } else {
            // 外部接口
            String authType = (String) dynamicData.get(FormJsonEnum.AUTH_TYPE.getValue());
            if (StringUtils.isNotEmpty(authType) && !FormJsonEnum.AUTH_TYPE_NO.getValue().equals(authType)) {
                Map authParams = (Map) dynamicData.get(FormJsonEnum.AUTH_PARAMS.getValue());
                if (FormJsonEnum.AUTH_TYPE_BASIC.getValue().equals(authType)) {
                    // 取username和password
                    String username = (String) authParams.get(FormJsonEnum.AUTH_PARAMS_USERNAME.getValue());
                    String password = (String) authParams.get(FormJsonEnum.AUTH_PARAMS_PASSWORD.getValue());
                    String encode = Base64Utils.encode(username + FormJsonEnum.SEPARTOR.getValue() + password);
                    authMap.put(FormJsonEnum.HEADER_AUTHORIZATION.getValue(), FormJsonEnum.HEADER_BASIC.getValue() + encode);
                } else if (FormJsonEnum.AUTH_TYPE_BEARER.getValue().equals(authType)) {
                    String token = (String) authParams.get(FormJsonEnum.AUTH_PARAMS_TOKEN.getValue());
                    authMap.put(FormJsonEnum.HEADER_AUTHORIZATION.getValue(), FormJsonEnum.HEADER_BEARER.getValue() + token);
                }
            }
        }
        Map<String, Object> jsonObject = new HashMap<>();
        // 目前只支持GET方式和无参的POST方式
        if (HttpMethod.GET.name().equalsIgnoreCase(method)) {
            // 外部接口必须数据需返回在data中
            jsonObject = HttpClientUtils.restGet(url, multiParamMap, authMap);
        } else if (HttpMethod.POST.name().equalsIgnoreCase(method)) {
            jsonObject = HttpClientUtils.restPost(url, null, authMap);
        }
        Object object = jsonObject.get("data");
        if (object instanceof Map<?, ?>) {
            return JsonUtils.deserialize(object, new TypeReference<Map<String, Object>>() {});
        } else if (object instanceof List<?>) {
            return JsonUtils.deserialize(object, new TypeReference<List<?>>() {});
        }
        return null;
    }

}
