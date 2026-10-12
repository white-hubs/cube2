package com.guhecloud.cube.biz.collection.analysis.gm.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.GmThirdDeptTreeDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.GmThirdUserDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.GmThirdUserPageDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.GmUserSyncConfigPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.NumberUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 光明三方人员接口客户端
 */
@Slf4j
@Component
public class GmUserSyncApiClient {
    private static final int SUCCESS_CODE = 200;
    private static final int DEFAULT_PAGE_SIZE = 200;
    private static final String USER_LIST_PATH = "/basic/open/system/user/list";
    private static final String DEPT_TREE_PATH = "/basic/open/system/dept/treeselect";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<GmThirdUserDTO> listAllUsers(GmUserSyncConfigPO config, LocalDateTime syncEndTime) {
        int pageSize = config.getPageSize() == null || config.getPageSize() <= 0 ? DEFAULT_PAGE_SIZE : config.getPageSize();
        List<GmThirdUserDTO> users = new ArrayList<>();
        int pageNum = 1;
        while (true) {
            GmThirdUserPageDTO page = listUsers(config, pageNum, pageSize, syncEndTime);
            if (page == null || CollectionUtils.isEmpty(page.getRows())) {
                break;
            }
            users.addAll(page.getRows());
            long total = page.getTotal() == null ? users.size() : page.getTotal();
            if (users.size() >= total || page.getRows().size() < pageSize) {
                break;
            }
            pageNum++;
        }
        return users;
    }

    private GmThirdUserPageDTO listUsers(GmUserSyncConfigPO config, int pageNum, int pageSize, LocalDateTime syncEndTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        addTimeRangeParams(params, config, syncEndTime);
        String url = appendQuery(normalizeBaseUrl(config.getBaseUrl()) + USER_LIST_PATH, params);
        HttpResponse response = HttpRequest.post(url)
                .header("appId", config.getAppId())
                .header("appKey", config.getAppKey())
                .body(JSON.toJSONString(params))
                .contentType("application/json;charset=UTF-8")
                .execute();
        Assert.isUnknownError(response == null || !response.isOk(), "光明人员同步接口请求失败");
        JSONObject body = JSON.parseObject(response.body());
        Object code = body.get("code");
        String codeStr = String.valueOf(code);
        Assert.isUnknownError(!NumberUtils.isNumber(codeStr) || SUCCESS_CODE != Integer.parseInt(codeStr),
                              String.valueOf(body.get("msg")));
        log.debug("光明人员同步拉取用户 pageNum={} pageSize={} updateTimeBegin={} updateTimeEnd={} body={}",
                  pageNum, pageSize, params.get("updateTimeBegin"), params.get("updateTimeEnd"), response.body());
        Object data = body.get("data");
        return data == null ? null : JSON.parseObject(JSON.toJSONString(data), GmThirdUserPageDTO.class);
    }

    public List<GmThirdDeptTreeDTO> listChangedDeptTree(GmUserSyncConfigPO config, LocalDateTime syncEndTime) {
        Map<String, Object> params = new HashMap<>();
        addTimeRangeParams(params, config, syncEndTime);
        return listDeptTree(config, params, "光明部门树增量同步接口请求失败");
    }

    public List<GmThirdDeptTreeDTO> listFullDeptTree(GmUserSyncConfigPO config) {
        return listDeptTree(config, new HashMap<>(), "光明部门树同步接口请求失败");
    }

    private List<GmThirdDeptTreeDTO> listDeptTree(GmUserSyncConfigPO config, Map<String, Object> params, String errorMessage) {
        String url = appendQuery(normalizeBaseUrl(config.getBaseUrl()) + DEPT_TREE_PATH, params);
        HttpResponse response = HttpRequest.post(url)
                .header("appId", config.getAppId())
                .header("appKey", config.getAppKey())
                .body(JSON.toJSONString(params))
                .contentType("application/json;charset=UTF-8")
                .execute();
        Assert.isUnknownError(response == null || !response.isOk(), errorMessage);
        JSONObject body = JSON.parseObject(response.body());
        Object code = body.get("code");
        String codeStr = String.valueOf(code);
        Assert.isUnknownError(!NumberUtils.isNumber(codeStr) || SUCCESS_CODE != Integer.parseInt(codeStr),
                              String.valueOf(body.get("msg")));
        log.debug("光明人员同步拉取部门树 updateTimeBegin={} updateTimeEnd={} body={}",
                  params.get("updateTimeBegin"), params.get("updateTimeEnd"), response.body());
        Object data = body.get("data");
        return data == null ? new ArrayList<>() : JSON.parseArray(JSON.toJSONString(data), GmThirdDeptTreeDTO.class);
    }

    private void addTimeRangeParams(Map<String, Object> params, GmUserSyncConfigPO config, LocalDateTime syncEndTime) {
        params.put("updateTimeBegin", formatDateTime(config.getLastSyncTime()));
        params.put("updateTimeEnd", formatDateTime(syncEndTime));
    }

    private String formatDateTime(LocalDateTime time) {
        return time == null ? "" : DATE_TIME_FORMATTER.format(time);
    }

    private String normalizeBaseUrl(String baseUrl) {
        Assert.isNotAcceptable(StringUtils.isBlank(baseUrl), "光明人员同步 baseUrl 未配置");
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    private String appendQuery(String url, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuilder builder = new StringBuilder(url);
        builder.append(url.contains("?") ? "&" : "?");
        params.forEach((key, value) -> builder.append(encode(key))
                .append("=")
                .append(encode(String.valueOf(value)))
                .append("&"));
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
