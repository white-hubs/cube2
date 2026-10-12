package com.guhecloud.cube.customization.xialing.server.liangcang.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.HttpHeader;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.JsonBody;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.JsonKeyCommon;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.ThirdPartyApi;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseDeviceResDTO;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseInfoCacheService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** 智慧仓库 warehouseInfo（POST）。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseInfoApiClient {

    private final RestTemplate restTemplate;
    private final WarehouseTokenProvider warehouseTokenProvider;
    private final WarehouseInfoCacheService warehouseInfoCacheService;

    public Optional<WarehouseDeviceResDTO> fetchWarehouseInfo(long warehouseId) {
        return Optional.ofNullable(warehouseInfoCacheService.listPageItemsByIds(Set.of(warehouseId)).get(warehouseId))
                .map(WarehouseInfoApiClient::toWarehouseDeviceRes);
    }

    public Optional<WarehouseDeviceResDTO> fetchRemoteWarehouseInfo(long warehouseId) {
        var url = trimTrailingSlash(warehouseTokenProvider.getBaseUrl()) + ThirdPartyApi.PATH_WAREHOUSE_INFO;

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeader.X_TOKEN, warehouseTokenProvider.getToken());

        Map<String, Object> body = new HashMap<>();
        body.put(JsonBody.WAREHOUSE_ID, warehouseId);

        try {
            log.info("三方接口请求 api=warehouseInfo method=POST url={} body={}", url, body);
            var resp = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
            log.info("三方接口响应 api=warehouseInfo status={} body={}", resp.getStatusCode(), resp.getBody());
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                log.warn("warehouseInfo 非成功响应: status={} warehouseId={}", resp.getStatusCode(), warehouseId);
                return Optional.empty();
            }
            return parseBody(resp.getBody());
        } catch (Exception e) {
            log.error("warehouseInfo 请求失败 warehouseId={}", warehouseId, e);
            return Optional.empty();
        }
    }

    private static WarehouseDeviceResDTO toWarehouseDeviceRes(LiangcangWarehousePageItemVO item) {
        var d = new WarehouseDeviceResDTO();
        d.setId(item.getId() != null ? item.getId().intValue() : null);
        d.setParentId(item.getParentId());
        d.setName(item.getName());
        d.setCategoryName(item.getCategoryName());
        d.setTotalVolume(item.getTotalVolume());
        d.setQualityGrade(item.getQualityGrade());
        d.setWarehouseHeight(item.getWarehouseHeight());
        d.setEnabledScan(item.getEnabledScan());
        d.setScanCorn(item.getScanCorn());
        d.setMaterialVolume(item.getMaterialVolume());
        d.setMaterialWeight(item.getMaterialWeight());
        d.setUsageRate(item.getUsageRate());
        d.setCameraSetting(item.getCameraSetting());
        d.setSetting(item.getSetting());
        return d;
    }

    private static String trimTrailingSlash(String b) {
        if (b.endsWith("/")) {
            return b.substring(0, b.length() - 1);
        }
        return b;
    }

    private Optional<WarehouseDeviceResDTO> parseBody(String body) {
        JsonNode root = JsonUtils.deserialize(body);
        if (root == null) {
            return Optional.empty();
        }
        JsonNode data = root.get(JsonKeyCommon.DATA);
        if (data == null || data.isNull()) {
            data = root.get(JsonKeyCommon.RESULT);
        }
        if (data == null || data.isNull()) {
            if (root.has(JsonKeyCommon.ID) || root.has(JsonKeyCommon.NAME)) {
                data = root;
            }
        }
        if (data == null || data.isNull()) {
            return Optional.empty();
        }
        return Optional.of(JsonUtils.deserialize(data, WarehouseDeviceResDTO.class));
    }
}
