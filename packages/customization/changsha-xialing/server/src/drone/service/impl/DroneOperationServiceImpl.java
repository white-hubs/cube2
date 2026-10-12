package com.guhecloud.cube.customization.xialing.server.drone.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.guhecloud.cube.customization.xialing.server.drone.auth.XianingBizTokenProvider;
import com.guhecloud.cube.customization.xialing.server.drone.service.DroneOperationService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneAlternatePointParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneCameraZoomParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneControlPermissionParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneFlyActionParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneGimbalAngleParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneHeadingParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneNestCommandParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DronePointFlightMoreParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DronePointFlightParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneReturnAltitudeParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTakeOffParam;
import com.guhecloud.subsystem.requester.sdk.param.drone.DroneTargetZoomParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 无人机操作服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DroneOperationServiceImpl implements DroneOperationService {
    private static final String AUTH_EXPIRED_CODE = "UAP-AUTH-402";
    private static final int SUCCESS_CODE = 200;
    private static final int DRONE_PERMISSION_OWNED_CODE = 148102;
    /**
     * 获取飞机控制权限接口路径。
     */
    private static final String APPLY_DRONE_CONTROL_PATH = "/prod-api/system/control/applyDrone";
    /**
     * 释放控制权限接口路径。
     */
    private static final String RELEASE_CONTROL_PATH = "/prod-api/system/control/release";
    /**
     * 飞机开机接口路径。
     */
    private static final String POWER_ON_PATH = "/prod-api/nest/drone/cmd/boot/open/{sn}";
    /**
     * 飞行操控接口路径。
     */
    private static final String FLY_ACTION_PATH = "/prod-api/nest/nest/cmd/flyAction/{sn}/{type}";
    /**
     * 一键起飞接口路径。
     */
    private static final String TAKE_OFF_PATH = "/prod-api/nest/nest/cmd/task/start/manual/{sn}/{scheduleId}";
    /**
     * 指点飞行（新）接口路径。
     */
    private static final String POINT_FLIGHT_MORE_PATH = "/prod-api/nest/drone/cmd/boot/giveDirectionsMore/flight";
    /**
     * 指点飞行（旧）接口路径。
     */
    private static final String POINT_FLIGHT_PATH = "/prod-api/nest/nest/cmd/boot/giveDirections/flight";
    /**
     * 精准返航接口路径。
     */
    private static final String PRECISION_RETURN_PATH = "/prod-api/nest/drone/cmd/boot/precision/{sn}";
    /**
     * 返航高度设置接口路径。
     */
    private static final String RETURN_ALTITUDE_PATH = "/prod-api/nest//nest/cmd/set/returnAltitude";
    /**
     * 备降点设置接口路径。
     */
    private static final String ALTERNATE_POINT_PATH = "/prod-api/nest/nest/cmd/upEquipment/coordinate/{sn}";
    /**
     * 取消备降点保护接口路径。
     */
    private static final String CANCEL_ALTERNATE_POINT_PROTECTION_PATH = "/prod-api/nest/nest/cmd/setActive/{sn}/46";
    /**
     * 紧急悬停接口路径。
     */
    private static final String HOVER_PATH = "/prod-api/nest/drone/cmd/boot/hover/{sn}";
    /**
     * 拉框放大接口路径。
     */
    private static final String TARGET_ZOOM_PATH = "/prod-api/nest/drone/cmd/ptz/targetZoom";
    /**
     * 飞机航向角设置接口路径。
     */
    private static final String HEADING_PATH = "/prod-api/nest/drone/cmd/headingSet/{sn}/{angleType}/{angle}";
    /**
     * 云台俯仰方位角设置接口路径。
     */
    private static final String GIMBAL_ANGLE_PATH = "/prod-api/nest/drone/cmd/camera/angle/{sn}/{podId}/{fyangle}/{fwangle}";
    /**
     * 相机变倍设置接口路径。
     */
    private static final String CAMERA_ZOOM_PATH = "/prod-api/nest/drone/cmd//cloudZoom/{sn}/{comId}/{model}/{value}";
    /**
     * 示例路径，暂无三方协议，不发起调用。
     */
    private static final String LAND_EXAMPLE_PATH = "/prod-api/nest/drone/cmd/land/{sn}";
    /**
     * 示例路径，暂无三方协议，不发起调用。
     */
    private static final String CANCEL_RETURN_EXAMPLE_PATH = "/prod-api/nest/drone/cmd/return/cancel/{sn}";
    /**
     * 示例路径，暂无三方协议，不发起调用。
     */
    private static final String POWER_OFF_EXAMPLE_PATH = "/prod-api/nest/drone/cmd/boot/close/{sn}";

    private final RestTemplate restTemplate;
    private final XianingBizTokenProvider tokenProvider;

    /**
     * 设置无人机控制权限。
     *
     * @param param 权限控制参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean setDroneControlPermission(DroneControlPermissionParam param) {
        assertParam(param, "飞机控制权限");
        String nestSn = requiredNestSn(param.getNestSn(), "飞机控制权限");
        Assert.isParameterError(param.getAuthorized() == null, "飞机控制权限授权状态不能为空");
        if (Boolean.TRUE.equals(param.getAuthorized())) {
            return applyDroneControl(nestSn);
        }
        return releaseDroneControl(nestSn);
    }

    /**
     * 调用三方接口执行飞机开机。
     *
     * @param param 机库参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean powerOn(DroneNestCommandParam param) {
        assertParam(param, "飞机开机");
        String nestSn = requiredNestSn(param.getNestSn(), "飞机开机");
        return getCommand("飞机开机", replaceSn(POWER_ON_PATH, nestSn));
    }

    /**
     * 调用三方接口执行飞行操控。
     *
     * @param param 飞行操控参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean flyAction(DroneFlyActionParam param) {
        assertParam(param, "飞行操控");
        String nestSn = requiredNestSn(param.getNestSn(), "飞行操控");
        Assert.isParameterError(!(param.getType() != null && param.getType() >= 0 && param.getType() <= 7), "飞行操控类型必须在0到7之间");
        String path = FLY_ACTION_PATH.replace("{sn}", nestSn).replace("{type}", String.valueOf(param.getType()));
        return getCommand("飞行操控", path);
    }

    /**
     * 调用三方接口执行一键起飞。
     *
     * @param param 起飞参数（含前端传入的 scheduleId）
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean takeOff(DroneTakeOffParam param) {
        assertParam(param, "一键起飞");
        String nestSn = requiredNestSn(param.getNestSn(), "一键起飞");
        Assert.isParameterError(param.getScheduleId() == null, "一键起飞调度ID不能为空");
        String path = TAKE_OFF_PATH.replace("{sn}", nestSn).replace("{scheduleId}", String.valueOf(param.getScheduleId()));
        return getCommand("一键起飞", path);
    }

    /**
     * 调用三方新指点飞行接口。
     *
     * @param param 指点飞行参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean pointFlightMore(DronePointFlightMoreParam param) {
        assertParam(param, "指点飞行");
        String nestSn = requiredNestSn(param.getNestSn(), "指点飞行");
        Assert.isParameterError(
                !(param.getFlyType() != null && (param.getFlyType() == 1 || param.getFlyType() == 2)), "指点飞行方式必须为1或2");
        Assert.isParameterError(!(param.getHeight() != null && param.getHeight() >= 5), "指点飞行高度不能小于5米");
        Assert.isParameterError(param.getLatitude() == null, "指点飞行纬度不能为空");
        Assert.isParameterError(param.getLongitude() == null, "指点飞行经度不能为空");
        Assert.isParameterError(param.getLevelSpeed() == null, "指点飞行水平速度不能为空");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("flyType", param.getFlyType());
        body.put("height", param.getHeight());
        body.put("latitude", param.getLatitude());
        body.put("levelSpeed", param.getLevelSpeed());
        body.put("longitude", param.getLongitude());
        body.put("sn", nestSn);
        return postJsonCommand("指点飞行", POINT_FLIGHT_MORE_PATH, body);
    }

    /**
     * 调用三方旧指点飞行接口。
     *
     * @param param 指点飞行参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean pointFlight(DronePointFlightParam param) {
        assertParam(param, "指点飞行");
        String nestSn = requiredNestSn(param.getNestSn(), "指点飞行");
        Assert.isParameterError(param.getLatitude() == null, "指点飞行纬度不能为空");
        Assert.isParameterError(param.getLongitude() == null, "指点飞行经度不能为空");
        Assert.isParameterError(param.getHeight() == null, "指点飞行高度不能为空");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("longitude", param.getLongitude());
        body.put("latitude", param.getLatitude());
        body.put("sn", nestSn);
        body.put("height", param.getHeight());
        put(body, "heightType", param.getHeightType());
        put(body, "levelSpeed", param.getLevelSpeed());
        put(body, "upSpeed", param.getUpSpeed());
        put(body, "downSpeed", param.getDownSpeed());
        return postJsonCommand("指点飞行", POINT_FLIGHT_PATH, body);
    }

    /**
     * 调用三方接口执行精准返航。
     *
     * @param param 机库参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean precisionReturn(DroneNestCommandParam param) {
        assertParam(param, "精准返航");
        String nestSn = requiredNestSn(param.getNestSn(), "精准返航");
        return getCommand("精准返航", replaceSn(PRECISION_RETURN_PATH, nestSn));
    }

    /**
     * 调用三方接口设置返航高度。
     *
     * @param param 返航高度参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean setReturnAltitude(DroneReturnAltitudeParam param) {
        assertParam(param, "返航高度设置");
        String nestSn = requiredNestSn(param.getNestSn(), "返航高度设置");
        Assert.isParameterError(!(param.getReturnAltitude() != null && param.getReturnAltitude() > 0), "返航高度必须大于0");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("droneSn", nestSn);
        body.put("returnAltitude", param.getReturnAltitude());
        return postJsonCommand("返航高度设置", RETURN_ALTITUDE_PATH, body);
    }

    /**
     * 调用三方接口设置备降点。
     *
     * @param param 备降点参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean setAlternatePoint(DroneAlternatePointParam param) {
        assertParam(param, "备降点设置");
        String nestSn = requiredNestSn(param.getNestSn(), "备降点设置");
        Assert.isParameterError(param.getLongitude() == null, "机库经度不能为空");
        Assert.isParameterError(param.getLatitude() == null, "机库纬度不能为空");
        Assert.isParameterError(param.getLongitudeBak() == null, "备降经度不能为空");
        Assert.isParameterError(param.getLatitudeBak() == null, "备降纬度不能为空");
        String url = UriComponentsBuilder.fromUriString(tokenProvider.getBaseUrl() + replaceSn(ALTERNATE_POINT_PATH, nestSn))
                .queryParam("longitude", param.getLongitude())
                .queryParam("latitude", param.getLatitude())
                .queryParam("longitudeBak", param.getLongitudeBak())
                .queryParam("latitudeBak", param.getLatitudeBak())
                .build()
                .toUriString();
        return exchangeCommand("备降点设置", HttpMethod.GET, url, authEntity());
    }

    /**
     * 调用三方接口取消备降点保护。
     *
     * @param param 机库参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean cancelAlternatePointProtection(DroneNestCommandParam param) {
        assertParam(param, "取消备降点保护");
        String nestSn = requiredNestSn(param.getNestSn(), "取消备降点保护");
        return getCommand("取消备降点保护", replaceSn(CANCEL_ALTERNATE_POINT_PROTECTION_PATH, nestSn));
    }

    /**
     * 调用三方接口执行紧急悬停。
     *
     * @param param 机库参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean hover(DroneNestCommandParam param) {
        assertParam(param, "紧急悬停");
        String nestSn = requiredNestSn(param.getNestSn(), "紧急悬停");
        return getCommand("紧急悬停", replaceSn(HOVER_PATH, nestSn));
    }

    /**
     * 调用三方接口执行拉框放大。
     *
     * @param param 拉框参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean targetZoom(DroneTargetZoomParam param) {
        assertParam(param, "拉框放大");
        String nestSn = requiredNestSn(param.getNestSn(), "拉框放大");
        Assert.isParameterError(param.getComId() == null, "挂载ID不能为空");
        assertBetween(param.getStartX(), 0, 1920, "起点X坐标必须在0到1920之间");
        assertBetween(param.getEndX(), 0, 1920, "终点X坐标必须在0到1920之间");
        assertBetween(param.getStartY(), 0, 1080, "起点Y坐标必须在0到1080之间");
        assertBetween(param.getEndY(), 0, 1080, "终点Y坐标必须在0到1080之间");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("sn", nestSn);
        body.put("comId", param.getComId());
        body.put("startX", param.getStartX());
        body.put("startY", param.getStartY());
        body.put("endX", param.getEndX());
        body.put("endY", param.getEndY());
        return postJsonCommand("拉框放大", TARGET_ZOOM_PATH, body);
    }

    /**
     * 调用三方接口设置飞机航向角。
     *
     * @param param 航向角参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean setHeading(DroneHeadingParam param) {
        assertParam(param, "飞机航向角设置");
        String nestSn = requiredNestSn(param.getNestSn(), "飞机航向角设置");
        Assert.isParameterError(
                !(param.getAngleType() != null && (param.getAngleType() == 0 || param.getAngleType() == 1)), "航向角类型必须为0或1");
        Assert.isParameterError(param.getAngle() == null, "航向角不能为空");
        String path = HEADING_PATH.replace("{sn}", nestSn)
                .replace("{angleType}", String.valueOf(param.getAngleType()))
                .replace("{angle}", String.valueOf(param.getAngle()));
        return getCommand("飞机航向角设置", path);
    }

    /**
     * 调用三方接口设置云台俯仰方位角。
     *
     * @param param 云台角度参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean setGimbalAngle(DroneGimbalAngleParam param) {
        assertParam(param, "云台俯仰方位角设置");
        String nestSn = requiredNestSn(param.getNestSn(), "云台俯仰方位角设置");
        Assert.isParameterError(param.getPodId() == null, "挂载编号不能为空");
        assertExclusiveBetween(param.getFyangle(), -90, 30, "云台俯仰角必须在-90到30之间");
        assertExclusiveBetween(param.getFwangle(), -180, 180, "云台方位角必须在-180到180之间");
        String path = GIMBAL_ANGLE_PATH.replace("{sn}", nestSn)
                .replace("{podId}", String.valueOf(param.getPodId()))
                .replace("{fyangle}", String.valueOf(param.getFyangle()))
                .replace("{fwangle}", String.valueOf(param.getFwangle()));
        return getCommand("云台俯仰方位角设置", path);
    }

    /**
     * 调用三方接口设置相机变倍。
     *
     * @param param 相机变倍参数
     *
     * @return {@code true} 表示指令执行成功
     */
    @Override
    public Boolean setCameraZoom(DroneCameraZoomParam param) {
        assertParam(param, "相机变倍设置");
        String nestSn = requiredNestSn(param.getNestSn(), "相机变倍设置");
        Assert.isParameterError(param.getComId() == null, "挂载ID不能为空");
        Assert.isParameterError(!(param.getModel() != null && (param.getModel() == 1 || param.getModel() == 2)), "相机镜头类型必须为1或2");
        assertBetween(param.getValue(), 0, 35, "相机变倍倍率必须在0到35之间");
        String path = CAMERA_ZOOM_PATH.replace("{sn}", nestSn)
                .replace("{comId}", String.valueOf(param.getComId()))
                .replace("{model}", String.valueOf(param.getModel()))
                .replace("{value}", String.valueOf(param.getValue()));
        return getCommand("相机变倍设置", path);
    }

    /**
     * 暂无三方协议，不对外使用。示例URL：http://{{ipport}}/prod-api/nest/drone/cmd/land/{sn}
     */
    @Override
    public Boolean land(DroneNestCommandParam param) {
        assertParam(param, "降落");
        String nestSn = requiredNestSn(param.getNestSn(), "降落");
        String exampleUrl = exampleUrl(LAND_EXAMPLE_PATH, nestSn);
        log.debug("降落暂无三方协议，示例url={}", exampleUrl);
        Assert.isParameterError(true, "降落暂无三方协议，暂不支持");
        return Boolean.FALSE;
    }

    /**
     * 暂无三方协议，不对外使用。示例URL：http://{{ipport}}/prod-api/nest/drone/cmd/return/cancel/{sn}
     */
    @Override
    public Boolean cancelReturn(DroneNestCommandParam param) {
        assertParam(param, "取消返航");
        String nestSn = requiredNestSn(param.getNestSn(), "取消返航");
        String exampleUrl = exampleUrl(CANCEL_RETURN_EXAMPLE_PATH, nestSn);
        log.debug("取消返航暂无三方协议，示例url={}", exampleUrl);
        Assert.isParameterError(true, "取消返航暂无三方协议，暂不支持");
        return Boolean.FALSE;
    }

    /**
     * 暂无三方协议，不对外使用。示例URL：http://{{ipport}}/prod-api/nest/drone/cmd/boot/close/{sn}
     */
    @Override
    public Boolean powerOff(DroneNestCommandParam param) {
        assertParam(param, "飞机关机");
        String nestSn = requiredNestSn(param.getNestSn(), "飞机关机");
        String exampleUrl = exampleUrl(POWER_OFF_EXAMPLE_PATH, nestSn);
        log.debug("飞机关机暂无三方协议，示例url={}", exampleUrl);
        Assert.isParameterError(true, "飞机关机暂无三方协议，暂不支持");
        return Boolean.FALSE;
    }

    /**
     * 申请飞机控制权限。
     *
     * @param nestSn 机库编号
     *
     * @return {@code true} 表示申请成功
     */
    private Boolean applyDroneControl(String nestSn) {
        String url = UriComponentsBuilder.fromUriString(tokenProvider.getBaseUrl() + APPLY_DRONE_CONTROL_PATH)
                .queryParam("sn", nestSn)
                .build()
                .toUriString();
        return exchangeCommand("获取飞机权限", HttpMethod.GET, url, authEntity(), DRONE_PERMISSION_OWNED_CODE);
    }

    /**
     * 释放飞机控制权限。
     *
     * @param nestSn 机库编号
     *
     * @return {@code true} 表示释放成功
     */
    private Boolean releaseDroneControl(String nestSn) {
        String url = UriComponentsBuilder.fromUriString(tokenProvider.getBaseUrl() + RELEASE_CONTROL_PATH)
                .queryParam("sn", nestSn)
                .queryParam("deviceType", "DRONE")
                .build()
                .toUriString();
        return exchangeCommand("释放飞机权限", HttpMethod.GET, url, authEntity());
    }

    /**
     * 执行通用 GET 控制命令。
     */
    private Boolean getCommand(String actionName, String path) {
        return exchangeCommand(actionName, HttpMethod.GET, tokenProvider.getBaseUrl() + path, authEntity());
    }

    /**
     * 执行通用 POST(JSON) 控制命令。
     */
    private Boolean postJsonCommand(String actionName, String path, Object body) {
        return exchangeCommand(actionName, HttpMethod.POST, tokenProvider.getBaseUrl() + path, jsonEntity(body));
    }

    /**
     * 执行三方控制请求并按统一规则解析返回值。
     */
    private Boolean exchangeCommand(String actionName, HttpMethod method, String url, HttpEntity<?> entity, int... extraSuccessCodes) {
        String body = exchangeForString(method, url, entity);
        JsonNode root = parseRoot(body, actionName);
        Assert.isParameterError(!isSuccess(root, extraSuccessCodes), actionName + "失败: " + errorMessage(root));
        JsonNode data = root.get("data");
        if (data == null || data.isNull()) {
            return Boolean.TRUE;
        }
        if (data.isBoolean()) {
            return data.booleanValue();
        }
        String text = data.asText();
        if (StringUtils.hasText(text)) {
            return Boolean.valueOf(text);
        }
        return Boolean.TRUE;
    }

    /**
     * 发起HTTP请求并在鉴权过期时重试一次。
     */
    private String exchangeForString(HttpMethod method, String url, HttpEntity<?> entity) {
        String body = restTemplate.exchange(url, method, entity, String.class).getBody();
        if (isAuthExpired(body)) {
            tokenProvider.evictToken();
            body = restTemplate.exchange(url, method, rebuildEntity(entity), String.class).getBody();
        }
        return body == null ? "" : body;
    }

    /**
     * 构造仅含鉴权头的请求实体。
     */
    private HttpEntity<?> authEntity() {
        return new HttpEntity<>(buildAuthHeaders(MediaType.APPLICATION_JSON));
    }

    /**
     * 构造 JSON 请求实体。
     */
    private HttpEntity<String> jsonEntity(Object body) {
        return new HttpEntity<>(JsonUtils.serialize(body), buildAuthHeaders(MediaType.APPLICATION_JSON));
    }

    /**
     * 构建三方请求头（token、租户、clientid）。
     */
    private HttpHeaders buildAuthHeaders(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(tokenProvider.getToken());
        headers.set("Tenant-Id", tokenProvider.getTenantId());
        headers.set("clientid", tokenProvider.getClientId());
        return headers;
    }

    /**
     * 使用最新 token 重建请求实体。
     */
    private HttpEntity<?> rebuildEntity(HttpEntity<?> entity) {
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(entity.getHeaders());
        headers.setBearerAuth(tokenProvider.getToken());
        return new HttpEntity<>(entity.getBody(), headers);
    }

    /**
     * 判断响应是否为 token 过期。
     */
    private boolean isAuthExpired(String body) {
        if (!StringUtils.hasText(body)) {
            return false;
        }
        JsonNode root = JsonUtils.deserialize(body);
        return root != null && AUTH_EXPIRED_CODE.equals(asText(root.get("errorCode")));
    }

    /**
     * 解析响应 JSON 根节点并校验非空。
     */
    private JsonNode parseRoot(String body, String actionName) {
        Assert.isParameterError(!StringUtils.hasText(body), actionName + "响应为空");
        JsonNode root = JsonUtils.deserialize(body);
        Assert.isParameterError(root == null, actionName + "响应无法解析");
        return root;
    }

    /**
     * 判定响应是否成功（code=200 或额外成功码）。
     */
    private boolean isSuccess(JsonNode root, int... extraSuccessCodes) {
        int code = root.path("code").asInt();
        if (code == SUCCESS_CODE) {
            return true;
        }
        for (int successCode : extraSuccessCodes) {
            if (code == successCode) {
                return true;
            }
        }
        return false;
    }

    /**
     * 提取响应错误信息，优先 msg，其次 errorCode。
     */
    private String errorMessage(JsonNode root) {
        String msg = asText(root.get("msg"));
        if (StringUtils.hasText(msg)) {
            return msg;
        }
        String errorCode = asText(root.get("errorCode"));
        if (StringUtils.hasText(errorCode)) {
            return errorCode;
        }
        return root.toString();
    }

    /**
     * 安全读取字符串字段，空串转 null。
     */
    private String asText(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        String text = node.asText();
        return StringUtils.hasText(text) ? text : null;
    }

    /**
     * 统一校验请求参数对象非空。
     */
    private void assertParam(Object param, String actionName) {
        Assert.isParameterError(param == null, actionName + "参数不能为空");
    }

    /**
     * 统一校验机库编号非空并返回去空格值。
     */
    private String requiredNestSn(String nestSn, String actionName) {
        Assert.isParameterError(!StringUtils.hasText(nestSn), actionName + "机库编号不能为空");
        return nestSn.trim();
    }

    /**
     * 校验整数范围（闭区间）。
     */
    private void assertBetween(Integer value, int min, int max, String message) {
        Assert.isParameterError(!(value != null && value >= min && value <= max), message);
    }

    /**
     * 校验小数范围（闭区间）。
     */
    private void assertBetween(Double value, double min, double max, String message) {
        Assert.isParameterError(!(value != null && value >= min && value <= max), message);
    }

    /**
     * 校验小数范围（开区间）。
     */
    private void assertExclusiveBetween(Double value, double min, double max, String message) {
        Assert.isParameterError(!(value != null && value > min && value < max), message);
    }

    /**
     * 替换路径模板中的机库编号占位符。
     */
    private String replaceSn(String path, String nestSn) {
        return path.replace("{sn}", nestSn);
    }

    /**
     * 仅在值非空时向请求体写入字段。
     */
    private void put(Map<String, Object> body, String key, Object value) {
        if (value != null) {
            body.put(key, value);
        }
    }

    /**
     * 构建示例 URL（用于暂无协议接口日志提示）。
     */
    private String exampleUrl(String path, String nestSn) {
        return "http://{{ipport}}" + replaceSn(path, nestSn);
    }
}
