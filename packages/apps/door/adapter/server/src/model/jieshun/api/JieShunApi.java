package com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api;

import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.config.JieShunConfig;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.dto.response.JieShunResponse;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.enums.ManufacturerEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.helper.JieShunAuthHelper;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.jieshun.api.helper.ManufacturerConfigHelper;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

/**
 * 捷顺公用的一些http对接接口
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JieShunApi {
    private final JieShunAuthHelper jieShunAuthHelper;
    private final RestTemplate restTemplate;
    private final ManufacturerConfigHelper manufacturerConfigHelper;

    /**
     * get方式 调用需要请求的方法
     */
    public <R> JieShunResponse<R> jieShunAuthPostApi(String uri, Object o) {
        //填充必须的header参数
        Map<String, String> headers = jieShunAuthHelper.getRequiredHeaders();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        //获取配置的厂家数据
        JieShunConfig jieShunConfig = manufacturerConfigHelper.getManufacturerConfig(ManufacturerEnum.JIE_SHUN, JieShunConfig.class);
        //用于访问三方的域名
        String host = jieShunConfig.getHost();
        log.info("【捷顺】请求域名:{}", host + uri);
        log.info("【捷顺】请求头参数:{}", httpHeaders);

        log.info("【捷顺】请求体参数:{}", o);
        HttpEntity<Object> entity = new HttpEntity<>(o, httpHeaders);

        try {
            //发送请求
            ResponseEntity<JieShunResponse<R>> responseResponseEntity = restTemplate.exchange(
                    host + uri, HttpMethod.POST, entity, new ParameterizedTypeReference<JieShunResponse<R>>() {});
            //判断是否http请求成功
            Assert.isUnknownError(!responseResponseEntity.getStatusCode().is2xxSuccessful(),
                                  "【捷顺】http请求失败,错误码：" + responseResponseEntity.getStatusCodeValue());
            log.info("【捷顺】响应结果：{}", responseResponseEntity.getBody());
            return Optional.ofNullable(responseResponseEntity.getBody())
                    .orElseThrow(() -> new ServiceException("【捷顺】内部错误,响应体为空"));
        } catch (RestClientException restClientException) {
            throw new ServiceException("【捷顺】http请求失败,错误原因：" + restClientException.getMessage());
        }

    }

}
