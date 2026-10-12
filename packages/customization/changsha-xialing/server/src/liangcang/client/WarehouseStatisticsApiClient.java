package com.guhecloud.cube.customization.xialing.server.liangcang.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.HttpHeader;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.HttpQuery;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.JsonKeyCommon;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.JsonKeyGoods;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.ThirdPartyApi;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseGoodsInfoRemoteItem;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseMeasureInfoRemoteDTO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.SetUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** 智慧仓库货量统计接口（GET）。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseStatisticsApiClient {
    private static final BigDecimal THIRD_PARTY_NUMBER_SCALE = BigDecimal.valueOf(100);

    private final RestTemplate restTemplate;
    private final WarehouseTokenProvider warehouseTokenProvider;

    public List<WarehouseGoodsInfoRemoteItem> fetchWarehouseGoodsInfo(long warehouseId, long startTimeSec, long endTimeSec) {
        var base = warehouseTokenProvider.getBaseUrl();
        var uri = UriComponentsBuilder.fromHttpUrl(trimTrailingSlash(base) + ThirdPartyApi.PATH_STATISTICS_GOODS)
                .queryParam(HttpQuery.WAREHOUSE_DATETIME_WAREHOUSE_ID, warehouseId)
                .queryParam(HttpQuery.WAREHOUSE_DATETIME_START, startTimeSec)
                .queryParam(HttpQuery.WAREHOUSE_DATETIME_END, endTimeSec)
                .build(true)
                .toUri();

        var headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeader.X_TOKEN, warehouseTokenProvider.getToken());
        try {
            log.info("三方接口请求 api=warehouseGoodsInfo method=GET url={} body=null", uri);
            var resp = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            log.info("三方接口响应 api=warehouseGoodsInfo status={} body={}", resp.getStatusCode(), resp.getBody());
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                log.warn("warehouseGoodsInfo 非成功响应: status={}", resp.getStatusCode());
                return Collections.emptyList();
            }
            return parseItems(resp.getBody());
        } catch (Exception e) {
            log.error("warehouseGoodsInfo 请求失败 warehouseId={} start={} end={}", warehouseId, startTimeSec, endTimeSec, e);
            return Collections.emptyList();
        }
    }

    public Optional<WarehouseMeasureInfoRemoteDTO> fetchMeasureInfo(long warehouseId) {
        var base = warehouseTokenProvider.getBaseUrl();
        var uri = UriComponentsBuilder.fromHttpUrl(trimTrailingSlash(base) + ThirdPartyApi.PATH_STATISTICS_MEASURE_INFO)
                .queryParam(HttpQuery.WAREHOUSE_DATETIME_WAREHOUSE_ID, warehouseId)
                .build(true)
                .toUri();

        var headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeader.X_TOKEN, warehouseTokenProvider.getToken());
        try {
            log.info("三方接口请求 api=measureInfo method=GET url={} body=null", uri);
            var resp = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            log.info("三方接口响应 api=measureInfo status={} body={}", resp.getStatusCode(), resp.getBody());
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                log.warn("measureInfo 非成功响应: status={}", resp.getStatusCode());
                return Optional.empty();
            }
            return parseMeasureInfo(resp.getBody());
        } catch (Exception e) {
            log.error("measureInfo 请求失败 warehouseId={}", warehouseId, e);
            return Optional.empty();
        }
    }

    public Map<Long, WarehouseMeasureInfoRemoteDTO> fetchMeasureInfoByWarehouseIds(Set<Long> warehouseIds) {
        if (SetUtils.isEmpty(warehouseIds)) {
            return Map.of();
        }
        Map<Long, WarehouseMeasureInfoRemoteDTO> out = new HashMap<>();
        for (Long warehouseId : warehouseIds) {
            if (warehouseId == null) {
                continue;
            }
            fetchMeasureInfo(warehouseId).ifPresent(item -> out.put(warehouseId, item));
        }
        return out;
    }

    private static String trimTrailingSlash(String base) {
        if (base.endsWith("/")) {
            return base.substring(0, base.length() - 1);
        }
        return base;
    }

    private List<WarehouseGoodsInfoRemoteItem> parseItems(String body) throws Exception {
        JsonNode root = JsonUtils.deserialize(body);
        JsonNode array = resolveArray(root);
        if (array == null || !array.isArray()) {
            return Collections.emptyList();
        }
        List<WarehouseGoodsInfoRemoteItem> out = new ArrayList<>();
        for (JsonNode n : array) {
            WarehouseGoodsInfoRemoteItem item = new WarehouseGoodsInfoRemoteItem();
            item.setAcquisitionDate(n.get(JsonKeyGoods.ACQUISITION_DATE));
            item.setVolumeValue(scaledDecimal(n, JsonKeyGoods.VOLUME_VALUE));
            item.setWeightValue(scaledDecimal(n, JsonKeyGoods.WEIGHT_VALUE));
            item.setVolumeDifference(scaledDecimal(n, JsonKeyGoods.VOLUME_DIFFERENCE));
            item.setWeightDifference(scaledDecimal(n, JsonKeyGoods.WEIGHT_DIFFERENCE));
            out.add(item);
        }
        return out;
    }

    private Optional<WarehouseMeasureInfoRemoteDTO> parseMeasureInfo(String body) throws Exception {
        JsonNode root = JsonUtils.deserialize(body);
        JsonNode data = root == null ? null : root.get(JsonKeyCommon.DATA);
        if (data == null || data.isNull()) {
            data = root == null ? null : root.get(JsonKeyCommon.RESULT);
        }
        if (data == null || data.isNull()) {
            data = root;
        }
        if (data == null || data.isNull()) {
            return Optional.empty();
        }
        return Optional.of(JsonUtils.deserialize(data, WarehouseMeasureInfoRemoteDTO.class));
    }

    private static JsonNode resolveArray(JsonNode root) {
        if (root == null) {
            return null;
        }
        if (root.isArray()) {
            return root;
        }
        if (root.has(JsonKeyCommon.DATA) && root.get(JsonKeyCommon.DATA).isArray()) {
            return root.get(JsonKeyCommon.DATA);
        }
        if (root.has(JsonKeyCommon.RECORDS) && root.get(JsonKeyCommon.RECORDS).isArray()) {
            return root.get(JsonKeyCommon.RECORDS);
        }
        if (root.has(JsonKeyCommon.RESULT) && root.get(JsonKeyCommon.RESULT).isArray()) {
            return root.get(JsonKeyCommon.RESULT);
        }
        return null;
    }

    private static BigDecimal scaledDecimal(JsonNode n, String field) {
        BigDecimal value = decimal(n, field);
        return value == null ? null : value.divide(THIRD_PARTY_NUMBER_SCALE).stripTrailingZeros();
    }

    private static BigDecimal decimal(JsonNode n, String field) {
        if (n == null || !n.has(field) || n.get(field).isNull()) {
            return null;
        }
        JsonNode v = n.get(field);
        if (v.isNumber()) {
            return v.decimalValue();
        }
        String s = v.asText();
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
