package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.client;

import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.config.JsCloudDataCenterConfig;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DigestUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 捷顺云数据中心客户端
 * 1. 登录获取令牌(tn)，默认有效期24小时
 * 2. 图片查询-返回URL，根据 filePath 获取 OSS 图片链接
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JsCloudDataCenterClient {

    private static final String LOGIN_PATH = "/api/login";
    private static final String PIC_SEARCH_PATH = "/api/pic/picSearchUrl";
    private static final String TOKEN_CACHE_KEY = "Park:JsCloud:DataCenter:Token:";
    /**
     * token 缓存有效期：固定 30s（按需求）
     */
    private static final int TOKEN_EXPIRE_SECONDS = 30;
    private static final String VE = "1.0";

    private final RestTemplate restTemplate;
    private final RedisTemplate redisTemplate;

    /**
     * 获取有效令牌，优先从缓存读取
     */
    public String getToken(JsCloudDataCenterConfig config, String cacheKey) {
        var cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null && StringUtils.isNotBlank(cached.toString())) {
            return cached.toString();
        }
        var loginReq = JsCloudDataCenterLoginRequest.builder()
                .pno(config.getDataCenterPno())
                .secret(config.getDataCenterSecret())
                .build();
        var baseUrl = config.getDataCenterBaseUrl();
        if (StringUtils.isBlank(baseUrl)) {
            baseUrl = "http://jhtdc.jslife.com.cn";
        }
        baseUrl = baseUrl.replaceAll("/$", "");
        var url = baseUrl + LOGIN_PATH;
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(loginReq, headers);
        try {
            var resp = restTemplate.postForEntity(url, entity, JsCloudDataCenterLoginResponse.class);
            var body = resp.getBody();
            if (body == null || body.getResultCode() == null || body.getResultCode() != 0) {
                throw new ServiceException("捷顺云数据中心登录失败: " + (body != null ? body.getMessage() : "响应为空"));
            }
            var tn = body.getTn();
            if (StringUtils.isBlank(tn)) {
                throw new ServiceException("捷顺云数据中心登录返回令牌为空");
            }
            // 固定缓存 30s，预留 1s 余量，避免并发下刚好过期
            redisTemplate.opsForValue().set(cacheKey, tn, Math.max(TOKEN_EXPIRE_SECONDS - 1, 1), TimeUnit.SECONDS);
            log.info("捷顺云数据中心登录成功, pno={}", config.getDataCenterPno());
            return tn;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("捷顺云数据中心登录异常: url={}", url, e);
            throw new ServiceException("捷顺云数据中心登录失败: " + e.getMessage());
        }
    }

    /**
     * 批量根据图片路径获取 OSS URL
     *
     * @param config    数据中心配置
     * @param cacheKey  令牌缓存 key
     * @param filePaths 图片路径列表
     *
     * @return 与 filePaths 顺序对应的 URL 列表，无图或失败时为 null
     */
    public List<String> getImageUrls(JsCloudDataCenterConfig config, String cacheKey, List<String> filePaths) {
        if (ListUtils.isEmpty(filePaths)) {
            return List.of();
        }
        var tn = getToken(config, cacheKey);
        var baseUrl = config.getDataCenterBaseUrl();
        if (StringUtils.isBlank(baseUrl)) {
            baseUrl = "http://jhtdc.jslife.com.cn";
        }
        baseUrl = baseUrl.replaceAll("/$", "");
        var url = baseUrl + PIC_SEARCH_PATH;

        var normalizedPaths = filePaths.stream()
                .map(this::normalizeFilePath)
                .toList();

        var dataItemsList = normalizedPaths.stream()
                .map(p -> Map.<String, String>of("filePath", p))
                .toList();
        var dataItemsStr = JsonUtils.serialize(dataItemsList);

        var ts = formatTs();
        var sn = buildSn(config.getDataCenterSecret(), ts, dataItemsStr);

        var req = JsCloudPicSearchRequest.builder()
                .pno(config.getDataCenterPno())
                .tn(tn)
                .ts(ts)
                .sn(sn)
                .ve(VE)
                .dataItems(dataItemsStr)
                .build();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(req, headers);
        log.info("捷顺云图片查询请求: url={}, request={}", url, JsonUtils.serialize(req));
        try {
            var resp = restTemplate.postForEntity(url, entity, JsCloudPicSearchResponse.class);
            var body = resp.getBody();
            log.info("捷顺云图片查询响应: url={}, response={}", url, JsonUtils.serialize(body));
            if (body == null || body.getResultCode() == null || body.getResultCode() != 0) {
                log.warn("捷顺云图片查询失败: resultCode={}, message={}",
                         body != null ? body.getResultCode() : null, body != null ? body.getMessage() : null);
                return null;
            }
            var urls = body.getAllImageUrls();
            return urls != null ? urls : List.of();
        } catch (Exception e) {
            log.error("捷顺云图片查询异常: url={}, filePaths={}", url, filePaths, e);
            return null;
        }
    }

    /**
     * 规范化图片路径：只去除开头的第一个 "/"，其他不变
     * 如 /p240167603/NISSP_IMG_PARK_IN/20260313/xxx -> p240167603/NISSP_IMG_PARK_IN/20260313/xxx
     */
    private String normalizeFilePath(String path) {
        if (path == null || path.isEmpty()) {
            return path;
        }
        return path.startsWith("/") ? path.substring(1) : path;
    }

    /**
     * 请求时间格式：yyyyMMddHHmmssSSS
     */
    private static String formatTs() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    /**
     * 签名规则：md5(secret + ts + dataItems)，结果转大写
     * dataItems 取中括号及其中内容，计算签名需将第一层转义符去除（即使用 JsonUtils.serialize 得到的原始 JSON 数组字符串）
     */
    private String buildSn(String secret, String ts, String dataItemsStr) {
        if (StringUtils.isBlank(secret)) {
            throw new ServiceException("捷顺云数据中心图片查询需配置 dataCenterSecret");
        }
        var signContent = (secret != null ? secret : "") + (ts != null ? ts : "") + (dataItemsStr != null ? dataItemsStr : "");
        return DigestUtils.md5Hex(signContent).toUpperCase();
    }
}
