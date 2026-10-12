package com.guhecloud.cube.biz.dispatch.server.map;//package com.guhecloud.cube.biz.dispatch.server.map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.dispatch.server.map.config.GaoDeConfig;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Component
public class GaoDeMapApi {

    @Autowired
    private GaoDeConfig gaoDeConfig;

    @Autowired
    private RestTemplate restTemplate;

    private final static String REGEO_URL = "https://restapi.amap.com/v3/geocode/regeo";

    public String regeoAddress(String gps) {
        try {
            var headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(null, headers);
            var uriBuilder = UriComponentsBuilder.fromHttpUrl(REGEO_URL)
                    .queryParam("key", gaoDeConfig.getAppKey())
                    .queryParam("location", gps)
                    .queryParam("output", "JSON");
            var finalUrl = uriBuilder.toUriString();
            var response = restTemplate.exchange(finalUrl, HttpMethod.GET, request, String.class);
            var body = JsonUtils.deserialize(response.getBody(), new TypeReference<Map<String, Object>>() {});
            var geocode = JsonUtils.deserialize(body.get("regeocode"), new TypeReference<Map<String, Object>>() {});
            return geocode.get("formatted_address").toString();
        } catch (Exception e) {
            log.error("高德gps转详细位置失败,失败原因", e);
        }
        return StringUtils.EMPTY_STRING;
    }
}