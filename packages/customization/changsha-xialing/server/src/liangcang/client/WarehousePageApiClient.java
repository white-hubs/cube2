package com.guhecloud.cube.customization.xialing.server.liangcang.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.HttpHeader;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.HttpQuery;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.ThirdPartyApi;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.WarehousePagePull;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.LiangcangWarehousePageQueryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.service.WarehouseInfoCacheService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehousePageItemVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.SetUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** 智慧仓库 warehousePage（GET，x-token）。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WarehousePageApiClient {

    private static final int ROOT_PARENT_ID = 0;
    private static final double THIRD_PARTY_NUMBER_SCALE = 100D;

    private final RestTemplate restTemplate;
    private final WarehouseTokenProvider warehouseTokenProvider;
    private final WarehouseInfoCacheService warehouseInfoCacheService;

    public WarehousePageRemoteResult fetchWarehousePage(LiangcangWarehousePageQueryParam param) {
        List<LiangcangWarehousePageItemVO> rows = warehouseInfoCacheService.listPageItems(param);
        var input =
                param != null && param.getInput() != null ? param.getInput() : new LiangcangWarehousePageQueryParam.WarehousePageInput();
        int pageNum = input.getPageNum() != null && input.getPageNum() > 0 ? input.getPageNum() : 1;
        int pageSize = input.getPageSize() != null && input.getPageSize() > 0 ? input.getPageSize() : 10;
        int fromIndex = Math.min((pageNum - 1) * pageSize, rows.size());
        int toIndex = Math.min(fromIndex + pageSize, rows.size());
        return new WarehousePageRemoteResult(rows.size(), rows.subList(fromIndex, toIndex));
    }

    public WarehousePageRemoteResult fetchRemoteWarehousePage(LiangcangWarehousePageQueryParam param) {
        var base = warehouseTokenProvider.getBaseUrl();
        var input =
                param != null && param.getInput() != null ? param.getInput() : new LiangcangWarehousePageQueryParam.WarehousePageInput();
        int pageNum = input.getPageNum() != null && input.getPageNum() > 0 ? input.getPageNum() : 1;
        int pageSize = input.getPageSize() != null && input.getPageSize() > 0 ? input.getPageSize() : 10;
        Integer parentId = input.getParentId();
        String search = input.getSearch();

        var builder = UriComponentsBuilder.fromHttpUrl(trimTrailingSlash(base) + ThirdPartyApi.PATH_WAREHOUSE_PAGE)
                .queryParam(HttpQuery.INPUT_PAGE_NUM, pageNum)
                .queryParam(HttpQuery.INPUT_PAGE_SIZE, pageSize);
        if (parentId != null) {
            builder.queryParam(HttpQuery.INPUT_PARENT_ID, parentId);
        }
        if (StringUtils.hasText(search)) {
            builder.queryParam(HttpQuery.INPUT_SEARCH, search);
        }
        var uri = builder.build(true).toUri();

        var headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(HttpHeader.X_TOKEN, warehouseTokenProvider.getToken());
        ObjectMapper lenient = lenientObjectMapper();
        try {
            log.info("三方接口请求 api=warehousePage method=GET url={} body=null", uri);
            var resp = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            log.info("三方接口响应 api=warehousePage status={} body={}", resp.getStatusCode(), resp.getBody());
            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                log.warn("warehousePage 非成功响应: status={}", resp.getStatusCode());
                return WarehousePageRemoteResult.empty();
            }
            return parsePage(lenient, resp.getBody());
        } catch (Exception e) {
            log.error("warehousePage 请求失败 uri={}", uri, e);
            return WarehousePageRemoteResult.empty();
        }
    }

    /** 按父子层级拉全量仓库，再按仓 id 匹配。 */
    public Map<Long, LiangcangWarehousePageItemVO> fetchPageItemsByIds(Set<Long> neededIds) {
        if (SetUtils.isEmpty(neededIds)) {
            return Map.of();
        }
        return warehouseInfoCacheService.listPageItemsByIds(neededIds);
    }

    public List<LiangcangWarehousePageItemVO> fetchRemoteAllWarehouseRowsByHierarchy() {
        List<LiangcangWarehousePageItemVO> parentRows = fetchRemotePagedWarehouseRows(ROOT_PARENT_ID);
        if (ListUtils.isEmpty(parentRows)) {
            return List.of();
        }
        Map<Long, LiangcangWarehousePageItemVO> byId = new HashMap<>();
        for (LiangcangWarehousePageItemVO parent : parentRows) {
            if (parent != null && parent.getId() != null) {
                byId.putIfAbsent(parent.getId(), parent);
            }
            Integer parentId = toParentId(parent);
            if (parentId == null) {
                continue;
            }
            List<LiangcangWarehousePageItemVO> childRows = fetchRemotePagedWarehouseRows(parentId);
            for (LiangcangWarehousePageItemVO child : childRows) {
                if (child != null && child.getId() != null) {
                    byId.putIfAbsent(child.getId(), child);
                }
            }
        }
        return new ArrayList<>(byId.values());
    }

    private List<LiangcangWarehousePageItemVO> fetchRemotePagedWarehouseRows(Integer parentId) {
        var query = new LiangcangWarehousePageQueryParam();
        var input = new LiangcangWarehousePageQueryParam.WarehousePageInput();
        input.setPageNum(1);
        input.setPageSize(WarehousePagePull.DEFAULT_PAGE_SIZE);
        input.setParentId(parentId);
        query.setInput(input);

        List<LiangcangWarehousePageItemVO> rows = new ArrayList<>();
        for (int pageNum = 1; pageNum <= WarehousePagePull.MAX_PAGES; pageNum++) {
            input.setPageNum(pageNum);
            var remote = fetchRemoteWarehousePage(query);
            List<LiangcangWarehousePageItemVO> batch = remote.getRecords();
            if (ListUtils.isEmpty(batch)) {
                break;
            }
            rows.addAll(batch);
            if (batch.size() < WarehousePagePull.DEFAULT_PAGE_SIZE) {
                break;
            }
            long total = remote.getTotal();
            if (total > 0 && rows.size() >= total) {
                break;
            }
        }
        return rows;
    }

    private static Integer toParentId(LiangcangWarehousePageItemVO parent) {
        if (parent == null || parent.getId() == null || parent.getId() > Integer.MAX_VALUE || parent.getId() < Integer.MIN_VALUE) {
            return null;
        }
        return parent.getId().intValue();
    }

    public Optional<LiangcangWarehousePageItemVO> fetchPageItemById(long warehouseId) {
        Map<Long, LiangcangWarehousePageItemVO> m = fetchPageItemsByIds(Set.of(warehouseId));
        return Optional.ofNullable(m.get(warehouseId));
    }

    private static ObjectMapper lenientObjectMapper() {
        ObjectMapper m = JsonUtils.getObjectMapper().copy();
        m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return m;
    }

    private static String trimTrailingSlash(String b) {
        if (b.endsWith("/")) {
            return b.substring(0, b.length() - 1);
        }
        return b;
    }

    private WarehousePageRemoteResult parsePage(ObjectMapper mapper, String body) throws Exception {
        JsonNode data = mapper.readTree(body).path("data");
        long total = data.path("totalCount").asLong();
        List<LiangcangWarehousePageItemVO> records = new ArrayList<>();
        JsonNode items = data.path("items");
        if (items.isArray()) {
            for (JsonNode n : items) {
                LiangcangWarehousePageItemVO item = mapper.treeToValue(n, LiangcangWarehousePageItemVO.class);
                normalizeNumberFields(item);
                records.add(item);
            }
        }
        return new WarehousePageRemoteResult(total, records);
    }

    private static void normalizeNumberFields(LiangcangWarehousePageItemVO item) {
        if (item == null) {
            return;
        }
        item.setTotalVolume(scaleDown(item.getTotalVolume()));
        item.setWarehouseHeight(scaleDown(item.getWarehouseHeight()));
        item.setMaterialVolume(scaleDown(item.getMaterialVolume()));
        item.setMaterialWeight(scaleDown(item.getMaterialWeight()));
    }

    private static Double scaleDown(Double value) {
        return value == null ? null : value / THIRD_PARTY_NUMBER_SCALE;
    }

    @Getter
    @RequiredArgsConstructor
    public static class WarehousePageRemoteResult {
        private final long total;
        private final List<LiangcangWarehousePageItemVO> records;

        static WarehousePageRemoteResult empty() {
            return new WarehousePageRemoteResult(0L, Collections.emptyList());
        }
    }
}
