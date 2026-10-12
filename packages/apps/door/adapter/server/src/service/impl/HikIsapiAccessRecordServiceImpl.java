package com.guhecloud.cube.srv.gen.adapter.access.control.server.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileUploadParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration.EventResultEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.isapi.config.HikIsapiConfig;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.hik.isapi.config.HikIsapiSystemConfig;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.HikIsapiAccessRecordService;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.SpringFileUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * 海康设备 ISAPI 门禁记录服务。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HikIsapiAccessRecordServiceImpl implements HikIsapiAccessRecordService {

    private static final String ISAPI_ACS_EVENT_PATH = "/ISAPI/AccessControl/AcsEvent?format=json";
    private static final int DEFAULT_PAGE_NO = 1;
    private static final int DEFAULT_PAGE_SIZE = 30;
    private static final int MAX_ISAPI_PAGE_SIZE = 100;
    private static final int MAX_MERGED_PAGE_SIZE = 1000;
    private static final int MAJOR_EVENT = 5;
    private static final String SYSTEM_CODE_SEPARATOR = "-";
    private static final String EVENT_PICTURE_SCENE_CODE = "IOT::ACCESS::EVENT_ATTACHMENTS";
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter EVENT_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Set<String> IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp", "icon", "svg");
    private static final Map<String, String> ISAPI_MINOR_NAMES = buildIsapiMinorNames();

    private final SubsystemRedisUtils subsystemRedisUtils;
    private final RestTemplate restTemplate;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final FileOperateClient fileOperateClient;

    @Override
    public Page<DoorEventsVo> getDoorEvents(DoorEventsParam param) {
        DoorEventsParam query = param == null ? new DoorEventsParam() : param;
        int pageNo = query.getPageNo() == null || query.getPageNo() < 1 ? DEFAULT_PAGE_NO : query.getPageNo();
        int pageSize = query.getPageSize() == null || query.getPageSize() < 1
                       ? DEFAULT_PAGE_SIZE : Math.min(query.getPageSize(), MAX_MERGED_PAGE_SIZE);
        long requiredSize = (long) pageNo * pageSize;
        if (requiredSize > Integer.MAX_VALUE) {
            throw new ServiceException("海康 ISAPI 门禁记录查询页码超出支持范围");
        }

        List<HikIsapiSystemConfig> systems = loadSystemConfigs();
        List<CompletableFuture<SystemEvents>> futures = systems.stream()
                .map(system -> CompletableFuture.supplyAsync(
                        () -> getSystemEvents(system, query, (int) requiredSize), threadPoolTaskExecutor))
                .toList();

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            List<SystemEvents> systemEvents = futures.stream().map(CompletableFuture::join).toList();
            return mergeSystemEvents(systemEvents, systems, pageNo, pageSize);
        } catch (CompletionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ServiceException serviceException) {
                throw serviceException;
            }
            throw new ServiceException("海康 ISAPI 多系统门禁记录查询失败：" + cause.getMessage());
        }
    }

    private Long uploadDoorEventPicture(HikIsapiSystemConfig system, SystemDoorEvent event) {
        String pictureUrl = resolvePictureUrl(system.getHost(), event.pictureUrl());
        BinaryRestResponse picture = doGetWithAuth(pictureUrl, system.getAccount(), system.getPwd());
        if (picture.body() == null || picture.body().length == 0) {
            throw new ServiceException("海康 ISAPI 门禁事件图片内容为空");
        }

        MediaType contentType = picture.headers().getContentType();
        String extension = resolveImageExtension(pictureUrl, contentType);
        String fileName = md5(system.getCode() + "-" + event.event().getEventId() + "-" + pictureUrl) + "." + extension;
        FileUploadParam uploadParam = new FileUploadParam();
        uploadParam.setTenant(SessionContextHolder.getRequester().getTenant());
        uploadParam.setSceneCode(EVENT_PICTURE_SCENE_CODE);
        try {
            uploadParam.setFile(SpringFileUtils.fileByteConvertMultipartFile(
                    fileName,
                    picture.body(),
                    contentType == null ? MediaType.IMAGE_JPEG_VALUE : contentType.toString()));
        } catch (IOException e) {
            throw new ServiceException("海康 ISAPI 门禁事件图片转换失败：" + e.getMessage());
        }
        FileVO file = fileOperateClient.uploadSustain(uploadParam);
        if (file == null || file.getId() == null) {
            throw new ServiceException("海康 ISAPI 门禁事件图片上传失败");
        }
        return file.getId();
    }

    private SystemEvents getSystemEvents(HikIsapiSystemConfig system, DoorEventsParam param, int requiredSize) {
        if (StringUtils.isNotBlank(param.getDoorName()) && !system.getDoorName().contains(param.getDoorName())) {
            return new SystemEvents(system.getCode(), 0, List.of());
        }
        String url = buildUrl(system.getHost(), ISAPI_ACS_EVENT_PATH);
        String searchId = buildSearchId(param);
        int position = 0;
        long total = 0;
        List<SystemDoorEvent> records = new ArrayList<>();

        while (records.size() < requiredSize) {
            JSONObject body = buildAcsEventRequest(param, searchId, position, MAX_ISAPI_PAGE_SIZE);
            log.info("海康 ISAPI 门禁记录采集，systemCode:{}，url:{}，position:{}，pageSize:{}",
                     system.getCode(), url, position, MAX_ISAPI_PAGE_SIZE);
            String result = doPostWithAuth(url, body.toJSONString(), system.getAccount(), system.getPwd());
            AcsEventResult page = parseAcsEventResult(result);
            total = page.total();
            if (CollectionUtils.isEmpty(page.rows())) {
                break;
            }

            page.rows().stream()
                    .map(row -> new SystemDoorEvent(
                            system.getCode(),
                            row.getString("pictureURL"),
                            toDoorEventsVo(row, system.getCode(), system.getDoorName())))
                    .filter(item -> matches(param, item.event()))
                    .forEach(records::add);
            position += page.rows().size();
            if (position >= total) {
                break;
            }
        }
        return new SystemEvents(system.getCode(), total, records);
    }

    private Page<DoorEventsVo> mergeSystemEvents(List<SystemEvents> systemEvents,
                                                 List<HikIsapiSystemConfig> systems,
                                                 int pageNo,
                                                 int pageSize) {
        Comparator<SystemDoorEvent> comparator = Comparator
                .comparing((SystemDoorEvent item) -> item.event().getEventTime(),
                           Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(SystemDoorEvent::systemCode)
                .thenComparing(item -> item.event().getEventId(), Comparator.nullsLast(Comparator.naturalOrder()));
        List<SystemDoorEvent> records = systemEvents.stream()
                .flatMap(system -> system.records().stream())
                .sorted(comparator)
                .toList();
        int offset = (pageNo - 1) * pageSize;
        List<SystemDoorEvent> pageRecords = offset >= records.size()
                                           ? List.of()
                                           : records.subList(offset, Math.min(offset + pageSize, records.size()));
        Map<String, HikIsapiSystemConfig> systemConfigMap = new HashMap<>();
        systems.forEach(system -> systemConfigMap.put(system.getCode(), system));
        List<DoorEventsVo> events = new ArrayList<>(pageRecords.size());
        for (SystemDoorEvent item : pageRecords) {
            DoorEventsVo event = item.event();
            if (StringUtils.isNotBlank(item.pictureUrl())) {
                try {
                    HikIsapiSystemConfig system = systemConfigMap.get(item.systemCode());
                    if (system == null) {
                        throw new ServiceException("未找到海康 ISAPI 系统配置：" + item.systemCode());
                    }
                    event.setPictureFileId(uploadDoorEventPicture(system, item));
                } catch (Exception e) {
                    log.warn("门禁事件图片转存失败，eventId:{}，systemCode:{}，原因:{}",
                             event.getEventId(), item.systemCode(), e.getMessage());
                }
            }
            events.add(event);
        }

        Page<DoorEventsVo> page = new Page<>(pageNo, pageSize);
        page.setTotal(systemEvents.stream().mapToLong(SystemEvents::total).sum());
        page.setRecords(events);
        return page;
    }

    private JSONObject buildAcsEventRequest(DoorEventsParam param, String searchId, int position, int pageSize) {
        JSONObject cond = new JSONObject();
        cond.put("searchID", searchId);
        cond.put("searchResultPosition", position);
        cond.put("maxResults", pageSize);
        cond.put("major", MAJOR_EVENT);
        cond.put("minor", resolveMinor(param));
        cond.put("timeReverseOrder", false);
        cond.put("picEnable", true);
        if (StringUtils.isNotBlank(param.getStartTime())) {
            cond.put("startTime", toIso8601(param.getStartTime()));
        }
        if (StringUtils.isNotBlank(param.getEndTime())) {
            cond.put("endTime", toIso8601(param.getEndTime()));
        }
        if (StringUtils.isNotBlank(param.getPersonName())) {
            cond.put("name", param.getPersonName());
        }

        JSONObject body = new JSONObject();
        body.put("AcsEventCond", cond);
        return body;
    }

    private int resolveMinor(DoorEventsParam param) {
        if (param == null || CollectionUtils.isEmpty(param.getEventTypes())) {
            return 0;
        }
        List<Integer> eventTypes = param.getEventTypes().stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (eventTypes.size() > 1) {
            throw new ServiceException("海康 ISAPI 门禁记录查询仅支持单个事件类型");
        }
        return eventTypes.isEmpty() ? 0 : eventTypes.getFirst();
    }

    private AcsEventResult parseAcsEventResult(String result) {
        JSONObject root = JSONObject.parseObject(result);
        JSONObject data = root.getJSONObject("AcsEvent");
        if (data == null) {
            throw new ServiceException("海康 ISAPI 门禁记录响应缺少 AcsEvent");
        }
        JSONArray rows = data.getJSONArray("InfoList");
        List<JSONObject> records = rows == null ? List.of() : rows.stream()
                                                              .filter(JSONObject.class::isInstance)
                                                              .map(JSONObject.class::cast)
                                                              .toList();
        return new AcsEventResult(data.getLongValue("totalMatches"), records);
    }

    private DoorEventsVo toDoorEventsVo(JSONObject row, String systemCode, String doorName) {
        String minor = row.getString("minor");
        String eventName = isapiMinorName(minor);
        String eventTime = row.getString("time");
        String doorNo = row.getString("doorNo");
        return DoorEventsVo.builder()
                .eventId(buildSystemCode(systemCode, row.getString("serialNo")))
                .doorIndexCode(buildSystemCode(systemCode, doorNo))
                .doorName(doorName)
                .eventType(minor)
                .personId(firstNotBlank(row.getString("employeeNoString"), row.getString("employeeNo")))
                .personName(row.getString("name"))
                .inAndOutType(resolveInAndOutType(row))
                .eventName(eventName)
                .eventTime(normalizeEventTime(eventTime))
                .eventWay(resolveEventWay(row, eventName))
                .eventResult(resolveEventResult(eventName))
                .cardNo(row.getString("cardNo"))
                .build();
    }

    private String normalizeEventTime(String eventTime) {
        if (StringUtils.isBlank(eventTime)) {
            return eventTime;
        }
        try {
            return EVENT_TIME_FORMATTER.format(
                    OffsetDateTime.parse(eventTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                            .atZoneSameInstant(BUSINESS_ZONE));
        } catch (DateTimeParseException ignored) {
            try {
                return EVENT_TIME_FORMATTER.format(
                        LocalDateTime.parse(eventTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } catch (DateTimeParseException ignoredAgain) {
                return eventTime;
            }
        }
    }

    private String buildSystemCode(String systemCode, String sourceCode) {
        return StringUtils.isBlank(sourceCode) ? sourceCode : systemCode + SYSTEM_CODE_SEPARATOR + sourceCode;
    }

    private Integer resolveInAndOutType(JSONObject row) {
        Integer doorNo = row.getInteger("doorNo");
        if (doorNo != null) {
            return doorNo;
        }
        Integer cardReaderNo = row.getInteger("cardReaderNo");
        if (cardReaderNo == null) {
            return null;
        }
        return switch (cardReaderNo) {
            case 1 -> 1;
            case 4 -> 2;
            default -> null;
        };
    }

    private String isapiMinorName(String minor) {
        return firstNotBlank(ISAPI_MINOR_NAMES.get(minor), ISAPI_MINOR_NAMES.get(toHexMinor(minor)), minor);
    }

    private String toHexMinor(String minor) {
        try {
            return String.valueOf(Integer.decode(minor));
        } catch (NumberFormatException e) {
            return minor;
        }
    }

    private String resolveEventWay(JSONObject row, String eventName) {
        String verifyMode = firstNotBlank(row.getString("currentVerifyMode"), row.getString("unlockType"));
        if (StringUtils.isBlank(verifyMode)) {
            return eventName;
        }
        return switch (verifyMode) {
            case "card" -> "刷卡";
            case "face" -> "人脸";
            case "fp", "fingerprint" -> "指纹";
            case "Pw", "password" -> "密码";
            case "qrcode", "qrCode" -> "二维码";
            case "cardOrfaceOrPw" -> "刷卡或人脸或密码";
            case "cardAndPw" -> "刷卡加密码";
            case "cardAndFp", "cardAndFingerPrint" -> "刷卡加指纹";
            case "faceAndPw" -> "人脸加密码";
            case "faceAndCard" -> "人脸加刷卡";
            default -> verifyMode;
        };
    }

    private String resolveEventResult(String eventName) {
        if (StringUtils.isBlank(eventName)) {
            return EventResultEnum.DO_REFUSE.name();
        }
        if (eventName.contains("失败") || eventName.contains("超时") || eventName.contains("异常")
            || eventName.contains("未授权") || eventName.contains("报警") || eventName.contains("错误")
            || eventName.contains("拒绝") || eventName.contains("无效") || eventName.contains("不匹配")
            || eventName.contains("不存在") || eventName.contains("未使能") || eventName.contains("未完成")
            || eventName.contains("被拦截")) {
            return EventResultEnum.DO_REFUSE.name();
        }
        if (eventName.contains("成功") || eventName.contains("通过") || eventName.contains("开门")
            || eventName.contains("门打开") || eventName.contains("门关闭")) {
            return EventResultEnum.DO_PASS.name();
        }
        return EventResultEnum.DO_REFUSE.name();
    }

    private boolean matches(DoorEventsParam param, DoorEventsVo item) {
        if (param == null || item == null) {
            return true;
        }
        if (StringUtils.isNotBlank(param.getDoorName())
            && (item.getDoorName() == null || !item.getDoorName().contains(param.getDoorName()))) {
            return false;
        }
        if (CollectionUtils.isNotEmpty(param.getDoorIndexCodes())
            && !param.getDoorIndexCodes().contains(item.getDoorIndexCode())) {
            return false;
        }
        return true;
    }

    private List<HikIsapiSystemConfig> loadSystemConfigs() {
        String key = SubsystemTypeConstants.DOOR_SERVER + ":hik:" + SessionContextHolder.getRequester().getTenant();
        SubsystemAdapterConfig config = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        if (config == null || config.getManufacturerJson() == null) {
            throw new ServiceException("海康 ISAPI 厂家配置未配置");
        }

        HikIsapiConfig hikConfig;
        try {
            hikConfig = JsonUtils.deserialize(config.getManufacturerJson(), HikIsapiConfig.class);
        } catch (RuntimeException e) {
            throw new ServiceException("海康 ISAPI 厂家配置格式错误");
        }
        if (hikConfig == null || CollectionUtils.isEmpty(hikConfig.getSystems())) {
            throw new ServiceException("海康 ISAPI 系统配置未配置");
        }

        List<HikIsapiSystemConfig> systems = hikConfig.getSystems().stream()
                .filter(Objects::nonNull)
                .filter(HikIsapiSystemConfig::isEnabled)
                .toList();
        if (CollectionUtils.isEmpty(systems)) {
            throw new ServiceException("海康 ISAPI 没有已启用的系统配置");
        }

        Set<String> codes = new HashSet<>();
        for (HikIsapiSystemConfig system : systems) {
            if (StringUtils.isBlank(system.getCode()) || StringUtils.isBlank(system.getHost())
                || StringUtils.isBlank(system.getAccount()) || StringUtils.isBlank(system.getPwd())
                || StringUtils.isBlank(system.getDoorName())) {
                throw new ServiceException("海康 ISAPI 系统编码、host、账号、密码和门禁名称均不能为空");
            }
            if (!codes.add(system.getCode())) {
                throw new ServiceException("海康 ISAPI 系统编码重复：" + system.getCode());
            }
        }
        return systems;
    }

    private String doPostWithAuth(String url, String body, String username, String password) {
        RestResponse first = post(url, body, null);
        if (first.is2xxSuccessful()) {
            return first.body();
        }
        if (first.statusCode() != HttpStatus.UNAUTHORIZED.value()) {
            throw new ServiceException("海康 ISAPI 请求失败，状态码：" + first.statusCode());
        }
        String challenge = first.headers().getFirst(HttpHeaders.WWW_AUTHENTICATE);
        if (StringUtils.isBlank(challenge)) {
            throw new ServiceException("海康 ISAPI 首次 401 响应缺少 WWW-Authenticate，无法生成 Digest 认证头");
        }
        log.info("海康 ISAPI Digest challenge:{}", challenge);
        String authorization = buildAuthorization(HttpMethod.POST, url, challenge, username, password);
        RestResponse second = post(url, body, authorization);
        if (second.is2xxSuccessful()) {
            return second.body();
        }
        throw new ServiceException("海康 ISAPI 认证请求失败，状态码：" + second.statusCode() + "，响应：" + second.body());
    }

    private RestResponse post(String url, String body, String authorization) {
        HttpHeaders headers = jsonHeaders();
        if (StringUtils.isNotBlank(authorization)) {
            headers.set(HttpHeaders.AUTHORIZATION, authorization);
        }
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
            return new RestResponse(response.getStatusCode().value(), response.getBody(), response.getHeaders());
        } catch (HttpStatusCodeException e) {
            return new RestResponse(e.getStatusCode().value(), e.getResponseBodyAsString(), e.getResponseHeaders());
        }
    }

    private BinaryRestResponse doGetWithAuth(String url, String username, String password) {
        BinaryRestResponse first = get(url, null);
        if (first.is2xxSuccessful()) {
            return first;
        }
        if (first.statusCode() != HttpStatus.UNAUTHORIZED.value()) {
            throw new ServiceException("海康 ISAPI 图片请求失败，状态码：" + first.statusCode());
        }
        String challenge = first.headers().getFirst(HttpHeaders.WWW_AUTHENTICATE);
        if (StringUtils.isBlank(challenge)) {
            throw new ServiceException("海康 ISAPI 图片请求缺少 Digest 认证参数");
        }
        String authorization = buildAuthorization(HttpMethod.GET, url, challenge, username, password);
        BinaryRestResponse second = get(url, authorization);
        if (second.is2xxSuccessful()) {
            return second;
        }
        throw new ServiceException("海康 ISAPI 图片认证请求失败，状态码：" + second.statusCode());
    }

    private BinaryRestResponse get(String url, String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.ALL));
        if (StringUtils.isNotBlank(authorization)) {
            headers.set(HttpHeaders.AUTHORIZATION, authorization);
        }
        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);
            return new BinaryRestResponse(response.getStatusCode().value(), response.getBody(), response.getHeaders());
        } catch (HttpStatusCodeException e) {
            HttpHeaders responseHeaders = e.getResponseHeaders() == null ? new HttpHeaders() : e.getResponseHeaders();
            return new BinaryRestResponse(e.getStatusCode().value(), e.getResponseBodyAsByteArray(), responseHeaders);
        }
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.ALL));
        return headers;
    }

    private String buildAuthorization(HttpMethod method, String url, String challenge, String username, String password) {
        String lower = challenge.toLowerCase(Locale.ROOT);
        if (!lower.startsWith("digest")) {
            throw new ServiceException("海康 ISAPI 不支持的认证方式：" + challenge);
        }
        Map<String, String> attrs = parseDigestChallenge(challenge.substring("Digest".length()));
        String realm = attrs.get("realm");
        String nonce = attrs.get("nonce");
        if (StringUtils.isBlank(realm) || StringUtils.isBlank(nonce)) {
            throw new ServiceException("海康 ISAPI Digest 认证参数不完整");
        }
        URI uri = URI.create(url);
        String requestUri = uri.getRawPath() + (uri.getRawQuery() == null ? "" : "?" + uri.getRawQuery());
        String qop = attrs.getOrDefault("qop", "");
        String selectedQop = qop.contains("auth") ? "auth" : null;
        String algorithm = attrs.getOrDefault("algorithm", "");
        String nc = "00000001";
        String cnonce = md5(UUID.randomUUID().toString());
        String ha1 = md5(username + ":" + realm + ":" + password);
        if ("MD5-sess".equalsIgnoreCase(algorithm)) {
            ha1 = md5(ha1 + ":" + nonce + ":" + cnonce);
        }
        String ha2 = md5(method.name() + ":" + requestUri);
        String response = selectedQop == null
                          ? md5(ha1 + ":" + nonce + ":" + ha2)
                          : md5(ha1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + selectedQop + ":" + ha2);

        StringBuilder header = new StringBuilder("Digest ");
        List<String> parts = new ArrayList<>();
        parts.add("username=\"" + username + "\"");
        parts.add("realm=\"" + realm + "\"");
        parts.add("nonce=\"" + nonce + "\"");
        parts.add("uri=\"" + requestUri + "\"");
        if (selectedQop != null) {
            parts.add("cnonce=\"" + cnonce + "\"");
            parts.add("nc=" + nc);
            parts.add("qop=\"" + selectedQop + "\"");
        }
        parts.add("response=\"" + response + "\"");
        if (StringUtils.isNotBlank(algorithm)) {
            parts.add("algorithm=\"" + algorithm + "\"");
        }
        if (StringUtils.isNotBlank(attrs.get("opaque"))) {
            parts.add("opaque=\"" + attrs.get("opaque") + "\"");
        }
        header.append(String.join(", ", parts));
        return header.toString();
    }

    private Map<String, String> parseDigestChallenge(String challenge) {
        Map<String, String> out = new HashMap<>();
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean readingKey = true;
        boolean quoted = false;
        for (int i = 0; i < challenge.length(); i++) {
            char c = challenge.charAt(i);
            if (readingKey && c == '=') {
                readingKey = false;
                continue;
            }
            if (!readingKey && c == '"') {
                quoted = !quoted;
                continue;
            }
            if (!quoted && c == ',') {
                putDigestAttr(out, key, value);
                readingKey = true;
                continue;
            }
            if (readingKey) {
                key.append(c);
            } else {
                value.append(c);
            }
        }
        putDigestAttr(out, key, value);
        return out;
    }

    private void putDigestAttr(Map<String, String> out, StringBuilder key, StringBuilder value) {
        String k = key.toString().trim();
        if (StringUtils.isNotBlank(k)) {
            out.put(k, value.toString().trim());
        }
        key.setLength(0);
        value.setLength(0);
    }

    private String buildSearchId(DoorEventsParam param) {
        String raw = JsonUtils.serialize(param == null ? new DoorEventsParam() : param);
        return "A" + md5(raw).substring(0, 31);
    }

    private String buildUrl(String host, String path) {
        return normalizeHost(host) + path;
    }

    private String normalizeHost(String host) {
        String base = host.trim();
        if (!base.startsWith("http://") && !base.startsWith("https://")) {
            base = "http://" + base;
        }
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base;
    }

    private String resolvePictureUrl(String host, String pictureUrl) {
        URI configured = URI.create(normalizeHost(host));
        URI origin = URI.create(configured.getScheme() + "://" + configured.getRawAuthority() + "/");
        URI resolved = origin.resolve(pictureUrl.trim());
        if (!sameEndpoint(origin, resolved)) {
            throw new ServiceException("海康 ISAPI 图片地址与配置系统地址不一致");
        }
        return resolved.toASCIIString();
    }

    private boolean sameEndpoint(URI configured, URI actual) {
        return configured.getScheme().equalsIgnoreCase(actual.getScheme())
               && configured.getHost().equalsIgnoreCase(actual.getHost())
               && effectivePort(configured) == effectivePort(actual);
    }

    private int effectivePort(URI uri) {
        if (uri.getPort() >= 0) {
            return uri.getPort();
        }
        return "https".equalsIgnoreCase(uri.getScheme()) ? 443 : 80;
    }

    private String resolveImageExtension(String pictureUrl, MediaType contentType) {
        String urlExtension = SpringFileUtils.getFileExtension(pictureUrl)
                .map(value -> value.toLowerCase(Locale.ROOT))
                .filter(IMAGE_EXTENSIONS::contains)
                .orElse(null);
        if (urlExtension != null) {
            return urlExtension;
        }
        if (contentType == null || !"image".equalsIgnoreCase(contentType.getType())) {
            return "jpg";
        }
        return switch (contentType.getSubtype().toLowerCase(Locale.ROOT)) {
            case "jpeg", "pjpeg" -> "jpg";
            case "svg+xml" -> "svg";
            case "x-icon", "vnd.microsoft.icon" -> "icon";
            default -> IMAGE_EXTENSIONS.contains(contentType.getSubtype().toLowerCase(Locale.ROOT))
                       ? contentType.getSubtype().toLowerCase(Locale.ROOT)
                       : "jpg";
        };
    }

    private String toIso8601(String time) {
        if (time.contains("T")) {
            return time;
        }
        return time.trim().replace(" ", "T") + "+08:00";
    }

    private static String firstNotBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private static String md5(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(String.valueOf(value).getBytes(StandardCharsets.UTF_8));
            StringBuilder out = new StringBuilder();
            for (byte b : bytes) {
                out.append(String.format("%02x", b));
            }
            return out.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not found", e);
        }
    }

    private static Map<String, String> buildIsapiMinorNames() {
        Map<String, String> names = new LinkedHashMap<>();
        putMinor(names, 0x01, "合法卡认证成功");
        putMinor(names, 0x02, "刷卡加密码认证成功");
        putMinor(names, 0x03, "刷卡加密码认证失败");
        putMinor(names, 0x04, "刷卡加密码认证超时");
        putMinor(names, 0x05, "刷卡加密码超次");
        putMinor(names, 0x06, "人员未分配权限");
        putMinor(names, 0x07, "无效时段");
        putMinor(names, 0x08, "卡号过期");
        putMinor(names, 0x09, "无此卡号");
        putMinor(names, 0x0a, "反潜回认证失败");
        putMinor(names, 0x0b, "互锁门未关闭");
        putMinor(names, 0x0c, "人员不属于多重认证群组");
        putMinor(names, 0x0d, "人员不在多重认证时间段内");
        putMinor(names, 0x0e, "多重认证模式超级密码认证失败");
        putMinor(names, 0x0f, "多重认证模式远程认证失败");
        putMinor(names, 0x10, "多重认证成功");
        putMinor(names, 0x11, "首人开门开始");
        putMinor(names, 0x12, "首人开门结束");
        putMinor(names, 0x13, "常开状态开始");
        putMinor(names, 0x14, "常开状态结束");
        putMinor(names, 0x15, "门打开（门锁）");
        putMinor(names, 0x16, "门关闭（门锁）");
        putMinor(names, 0x17, "开门按钮按下");
        putMinor(names, 0x18, "开门按钮放开");
        putMinor(names, 0x19, "门打开（门磁）");
        putMinor(names, 0x1a, "门关闭（门磁）");
        putMinor(names, 0x1b, "门异常打开（门磁）");
        putMinor(names, 0x1c, "门打开超时（门磁）");
        putMinor(names, 0x1d, "报警输出打开");
        putMinor(names, 0x1e, "报警输出关闭");
        putMinor(names, 0x1f, "常关状态开始");
        putMinor(names, 0x20, "常关状态结束");
        putMinor(names, 0x21, "多重认证需要远程开门");
        putMinor(names, 0x22, "多重认证超级密码认证成功");
        putMinor(names, 0x23, "多重认证重复认证");
        putMinor(names, 0x24, "多重认证超时");
        putMinor(names, 0x25, "门铃响");
        putMinor(names, 0x26, "指纹认证成功");
        putMinor(names, 0x27, "指纹认证失败");
        putMinor(names, 0x28, "刷卡加指纹认证成功");
        putMinor(names, 0x29, "刷卡加指纹认证失败");
        putMinor(names, 0x2a, "刷卡加指纹认证超时");
        putMinor(names, 0x2b, "刷卡加指纹加密码认证成功");
        putMinor(names, 0x2c, "刷卡加指纹加密码认证失败");
        putMinor(names, 0x2d, "刷卡加指纹加密码认证超时");
        putMinor(names, 0x2e, "指纹加密码认证成功");
        putMinor(names, 0x2f, "指纹加密码认证失败");
        putMinor(names, 0x30, "指纹加密码认证超时");
        putMinor(names, 0x31, "指纹不存在");
        putMinor(names, 0x32, "刷卡平台认证");
        putMinor(names, 0x33, "呼叫中心");
        putMinor(names, 0x34, "消防继电器导通触发门常开");
        putMinor(names, 0x35, "消防继电器恢复门恢复正常");
        putMinor(names, 0x36, "人脸加指纹认证成功");
        putMinor(names, 0x37, "人脸加指纹认证失败");
        putMinor(names, 0x38, "人脸加指纹认证超时");
        putMinor(names, 0x39, "人脸加密码认证成功");
        putMinor(names, 0x3a, "人脸加密码认证失败");
        putMinor(names, 0x3b, "人脸加密码认证超时");
        putMinor(names, 0x3c, "人脸加刷卡认证成功");
        putMinor(names, 0x3d, "人脸加刷卡认证失败");
        putMinor(names, 0x3e, "人脸加刷卡认证超时");
        putMinor(names, 0x3f, "人脸加密码加指纹认证成功");
        putMinor(names, 0x40, "人脸加密码加指纹认证失败");
        putMinor(names, 0x41, "人脸加密码加指纹认证超时");
        putMinor(names, 0x42, "人脸加刷卡加指纹认证成功");
        putMinor(names, 0x43, "人脸加刷卡加指纹认证失败");
        putMinor(names, 0x44, "人脸加刷卡加指纹认证超时");
        putMinor(names, 0x45, "工号加指纹认证成功");
        putMinor(names, 0x46, "工号加指纹认证失败");
        putMinor(names, 0x47, "工号加指纹认证超时");
        putMinor(names, 0x48, "工号加指纹加密码认证成功");
        putMinor(names, 0x49, "工号加指纹加密码认证失败");
        putMinor(names, 0x4a, "工号加指纹加密码认证超时");
        putMinor(names, 0x4b, "人脸认证成功");
        putMinor(names, 0x4c, "人脸认证失败");
        putMinor(names, 0x4d, "工号加人脸认证成功");
        putMinor(names, 0x4e, "工号加人脸认证失败");
        putMinor(names, 0x4f, "工号加人脸认证超时");
        putMinor(names, 0x50, "人脸识别失败");
        putMinor(names, 0x51, "首人授权开始");
        putMinor(names, 0x52, "首人授权结束");
        putMinor(names, 0x53, "门锁输入短路报警");
        putMinor(names, 0x54, "门锁输入断路报警");
        putMinor(names, 0x55, "门锁输入异常报警");
        putMinor(names, 0x56, "门磁输入短路报警");
        putMinor(names, 0x57, "门磁输入断路报警");
        putMinor(names, 0x58, "门磁输入异常报警");
        putMinor(names, 0x59, "开门按钮输入短路报警");
        putMinor(names, 0x5a, "开门按钮输入断路报警");
        putMinor(names, 0x5b, "开门按钮输入异常报警");
        putMinor(names, 0x5c, "门锁异常打开");
        putMinor(names, 0x5d, "门锁打开超时");
        putMinor(names, 0x5e, "首人未授权开门失败");
        putMinor(names, 0x5f, "呼梯继电器断开");
        putMinor(names, 0x60, "呼梯继电器闭合");
        putMinor(names, 0x61, "自动按键继电器断开");
        putMinor(names, 0x62, "自动按键继电器闭合");
        putMinor(names, 0x63, "按键梯控继电器断开");
        putMinor(names, 0x64, "按键梯控继电器闭合");
        putMinor(names, 0x65, "工号加密码认证成功");
        putMinor(names, 0x66, "工号加密码认证失败");
        putMinor(names, 0x67, "工号加密码认证超时");
        putMinor(names, 0x68, "真人检测失败");
        putMinor(names, 0x69, "人证比对成功");
        putMinor(names, 0x70, "人证比对失败");
        putMinor(names, 0x71, "非授权名单事件");
        putMinor(names, 0x75, "门状态常闭或休眠状态认证失败");
        putMinor(names, 0x76, "认证计划休眠模式认证失败");
        putMinor(names, 0x77, "卡加密校验失败");
        putMinor(names, 0x78, "反潜回服务器应答失败");
        putMinor(names, 0x82, "门常闭或休眠时开门按钮开门失败");
        putMinor(names, 0x83, "心跳事件");
        putMinor(names, 0x84, "门常闭或休眠时开门联动开门失败");
        putMinor(names, 0x89, "通行超时");
        putMinor(names, 0x8a, "误闯报警");
        putMinor(names, 0x8e, "设备升级本地人脸建模失败");
        putMinor(names, 0x8f, "逗留事件");
        putMinor(names, 0x90, "授权名单认证成功");
        putMinor(names, 0x91, "授权名单认证失败");
        putMinor(names, 0x92, "人脸采集事件（无认证，远程核验时上报）");
        putMinor(names, 0x93, "卡采集事件（无认证，远程核验时上报）");
        putMinor(names, 0x94, "密码认证失败超次");
        putMinor(names, 0x95, "身份证采集事件（无认证，远程核验时上报）");
        putMinor(names, 0x96, "密码认证失败");
        putMinor(names, 0x97, "密码不匹配");
        putMinor(names, 0x99, "组合认证成功");
        putMinor(names, 0x9a, "组合认证超时");
        putMinor(names, 0x9b, "认证方式不匹配");
        putMinor(names, 0x9c, "二维码认证成功");
        putMinor(names, 0x9d, "二维码认证失败");
        putMinor(names, 0x9e, "户主授权认证成功");
        putMinor(names, 0x9f, "蓝牙认证成功");
        putMinor(names, 0xa0, "蓝牙认证失败");
        putMinor(names, 0xb3, "动态验证码认证成功");
        putMinor(names, 0xb4, "动态验证码认证失败");
        putMinor(names, 0xb5, "密码认证成功");
        putMinor(names, 0xbe, "虹膜认证成功");
        putMinor(names, 0xbf, "虹膜认证失败");
        putMinor(names, 0xc0, "虹膜防伪检测失败");
        putMinor(names, 0xc3, "动态验证码认证过期");
        putMinor(names, 0xca, "指纹识别未使能认证失败");
        putMinor(names, 0xcb, "人脸识别未使能认证失败");
        putMinor(names, 0xcc, "预览时开门");
        putMinor(names, 0xd0, "属性异常认证失败（未戴口罩，安全帽，体温异常等属性异常）");
        putMinor(names, 0xd4, "人员认证次数已满");
        putMinor(names, 0xd5, "人脸建模升级兼容失败");
        putMinor(names, 0xd6, "设备开锁记录事件上报");
        putMinor(names, 0xd7, "门未开报警");
        putMinor(names, 0xd8, "门未关报警");
        putMinor(names, 0xd9, "二维码密钥验证失败");
        putMinor(names, 0xda, "人员通行事件");
        putMinor(names, 0xdb, "社保卡人证比对成功");
        putMinor(names, 0xdc, "社保卡人证比对失败");
        putMinor(names, 0xe0, "分控制器升级成功");
        putMinor(names, 0xe1, "分控制器升级失败");
        putMinor(names, 0xe2, "工号认证成功");
        putMinor(names, 0xe3, "NFC认证成功");
        putMinor(names, 0xe4, "NFC认证失败");
        putMinor(names, 0xe5, "公共密码开门");
        putMinor(names, 0xed, "自定义二维码认证成功");
        putMinor(names, 0xee, "自定义二维码认证失败");
        putMinor(names, 0xef, "NFC加密校验失败");
        putMinor(names, 0xf2, "门运行中暂停");
        putMinor(names, 0xf3, "NFC认证处理异常");
        putMinor(names, 0xf4, "已开启NFC加密校验下未加密NFC卡认证失败");
        putMinor(names, 0xf5, "门锁预关门事件");
        putMinor(names, 0xf6, "门未启用事件");
        putMinor(names, 0xf7, "门选择超时事件");
        putMinor(names, 0xf8, "遮挡报警");
        putMinor(names, 0xfa, "生物密钥卡认证成功");
        putMinor(names, 0xfb, "生物密钥卡认证失败");
        putMinor(names, 0x600, "智能锁指纹开门");
        putMinor(names, 0x601, "智能锁密码开门");
        putMinor(names, 0x602, "智能锁刷卡开门");
        putMinor(names, 0x603, "智能锁中心开门");
        putMinor(names, 0x604, "智能锁APP开门");
        putMinor(names, 0x605, "智能锁钥匙开门");
        putMinor(names, 0x606, "智能锁遥控器开门");
        putMinor(names, 0x607, "智能锁临时密码开门");
        putMinor(names, 0x608, "智能锁蓝牙开门");
        putMinor(names, 0x609, "智能锁多重开门");
        putMinor(names, 0x80d, "遥控器认证成功");
        putMinor(names, 0x80e, "遥控器认证失败");
        putMinor(names, 0x820, "开门中");
        putMinor(names, 0x822, "蓝牙密钥验证失败");
        putMinor(names, 0x825, "洗手未完成");
        return Map.copyOf(names);
    }

    private static void putMinor(Map<String, String> names, int code, String name) {
        names.put(String.valueOf(code), name);
    }

    private record AcsEventResult(long total, List<JSONObject> rows) {}

    private record SystemEvents(String systemCode, long total, List<SystemDoorEvent> records) {}

    private record SystemDoorEvent(String systemCode, String pictureUrl, DoorEventsVo event) {}

    private record RestResponse(int statusCode, String body, HttpHeaders headers) {
        private boolean is2xxSuccessful() {
            return statusCode >= 200 && statusCode < 300;
        }
    }

    private record BinaryRestResponse(int statusCode, byte[] body, HttpHeaders headers) {
        private boolean is2xxSuccessful() {
            return statusCode >= 200 && statusCode < 300;
        }
    }
}
