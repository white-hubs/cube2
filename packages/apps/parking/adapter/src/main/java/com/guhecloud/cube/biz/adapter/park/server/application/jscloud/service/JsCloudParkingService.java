package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.carsafe.enums.CarSafeFeeEnum;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.client.JsCloudDataCenterClient;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.config.JsCloudBlacklistConfig;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.config.JsCloudDataCenterConfig;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model.JsCloudBwListApiRequest;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model.JsCloudBwListApiResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model.JsCloudBwListResult;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model.JsCloudCommonErrorCode;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model.JsCloudResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.mq.message.JsCloudParkInDelayMessage;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.mq.message.JsCloudParkOutDelayMessage;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudBwListBizContent;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudBwListParam;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkChargeQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkInQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkInfoQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkOutQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.util.JsCloudSignUtils;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingInRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingOutRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingPayRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingInRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingOutRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingPayRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 捷顺云停车场接口
 * 参考文档: https://jsopen.jslife.com.cn/cloudopen/index.html#/documentCenter?id=241
 *
 * @author appet
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JsCloudParkingService extends AbstractParkingService {

    private static final String REDIS_KEY_PREFIX = "Park:JsCloud:";
    private static final String TOKEN_CACHE_KEY = "Park:JsCloud:DataCenter:Token:";
    private static final int REDIS_EXPIRE_DAYS = 7;
    /**
     * RocketMQ 延迟等级：需要在 broker 配置中对应到 20s
     */
    private static final int JS_CLOUD_DELAY_LEVEL_20S = 7;

    /**
     * 黑白灰名单接口编码
     */
    private static final String BW_LIST_METHOD = "tq.jzgk.monthCardApi.addCarBlack";

    private final RedisTemplate redisTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final RestTemplate restTemplate;
    private final SubsystemRedisUtils subsystemRedisUtils;
    private final JsCloudDataCenterClient jsCloudDataCenterClient;

    @Override
    public List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        log.info("捷顺云-车场信息==========================================");
        var tenant = SessionContextHolder.getRequester().getTenant();
        var parkingInfos = this.getParkingPlaceFromRedis(tenant);
        if (ListUtils.isEmpty(parkingInfos)) {
            return Collections.emptyList();
        }
        return parkingInfos.stream().map(this::toParkingInfoVO).collect(Collectors.toList());
    }

    private ParkingInfoVO toParkingInfoVO(JsCloudParkInfoQo qo) {
        var vo = new ParkingInfoVO();
        vo.setCode(ObjectUtils.ifNull(qo.getParkCode(), qo.getItemId()));
        vo.setName(qo.getParkName());
        vo.setTotalNum(ObjectUtils.ifNull(qo.getTotalSpace(), 0));
        vo.setTotalSpaceNum(ObjectUtils.ifNull(qo.getRemainSpace(), 0));
        vo.setTotalInNum(ObjectUtils.ifNull(qo.getTotalSpace(), 0) - ObjectUtils.ifNull(qo.getRemainSpace(), 0));
        return vo;
    }

    /**
     * 车位信息上报，保存到 Redis
     *
     * @param dataItems 车场车位信息列表
     * @param tenant    租户
     *
     * @return
     */
    public JsCloudResponse reportParkingInfoRecordService(List<JsCloudParkInfoQo> dataItems, Integer tenant) {
        try {
            if (CollectionUtils.isEmpty(dataItems)) {
                return JsCloudResponse.builder().resultCode(1).message("车场上报数据为空").build();
            }
            var key = REDIS_KEY_PREFIX + tenant + ":parkingInfo";
            redisTemplate.opsForValue().set(key, JsonUtils.serialize(dataItems), REDIS_EXPIRE_DAYS, TimeUnit.DAYS);
            log.info("捷顺云-车位数上报成功: tenant={}, count={}", tenant, dataItems.size());
            return JsCloudResponse.builder().resultCode(0).message("").build();
        } catch (Exception e) {
            log.error("捷顺云-车位数上报，异常：", e);
            return JsCloudResponse.builder().resultCode(1).message("车位数上报异常").build();
        }
    }

    /**
     * 从 Redis 获取车位数据
     */
    public List<JsCloudParkInfoQo> getParkingPlaceFromRedis(Integer tenant) {
        var key = REDIS_KEY_PREFIX + tenant + ":parkingInfo";
        var placeInfo = redisTemplate.opsForValue().get(key);
        if (placeInfo != null) {
            return JsonUtils.deserializeToList(placeInfo.toString(), JsCloudParkInfoQo.class);
        }
        return Collections.emptyList();
    }

    /**
     * 从车辆信息JSON中解析车牌颜色
     * 车辆信息包含 plateNo(车牌号)、plateColor(车牌颜色，取值见数据字典15: UNKNOW/WHITE/BLACK/BLUE/YELLOW/GREEN)
     */
    private String parsePlateColorFromVehicleInfo(String vehicleInfo) {
        if (StringUtils.isEmpty(vehicleInfo)) {
            return null;
        }
        try {
            var map = JsonUtils.deserialize(vehicleInfo, new TypeReference<Map<String, Object>>() {});
            if (map != null && map.containsKey("plateColor")) {
                var plateColor = map.get("plateColor");
                return plateColor != null ? plateColor.toString() : null;
            }
        } catch (Exception e) {
            log.debug("解析车辆信息plateColor失败: vehicleInfo={}", vehicleInfo, e);
        }
        return null;
    }

    /**
     * 入场记录上报
     * 图片地址通过数据中心接口获取：先登录获取令牌，再调用图片查询接口将 filePath 转为 OSS URL
     *
     * @param dataItems 入场记录列表
     * @param tenant    租户
     *
     * @return
     */
    public JsCloudResponse reportParkingInRecordService(List<JsCloudParkInQo> dataItems, Integer tenant) {
        try {
            if (CollectionUtils.isEmpty(dataItems)) {
                return JsCloudResponse.builder().resultCode(1).message("入场记录数据为空").build();
            }
            var msg = JsCloudParkInDelayMessage.builder().tenant(tenant).dataItems(new ArrayList<>(dataItems)).build();
            RocketMQContextHolder.asyncSend(JsCloudParkInDelayMessage.TOPIC, msg, JS_CLOUD_DELAY_LEVEL_20S, payload -> {
                log.error("捷顺云-入场记录延时消息发送失败: tenant={}, count={}", tenant, dataItems.size());
            });
            log.info("捷顺云-入场记录已发送延时消息: tenant={}, count={}", tenant, dataItems.size());
            return JsCloudResponse.builder().resultCode(0).message("").build();
        } catch (Exception e) {
            log.error("捷顺云-入场记录上报，异常：", e);
            return JsCloudResponse.builder().resultCode(1).message("入场记录上报异常").build();
        }
    }

    public void handleParkingInRecord(List<JsCloudParkInQo> dataItems, Integer tenant) {
        if (CollectionUtils.isEmpty(dataItems)) {
            return;
        }
        var pathToUrl = resolveImagePathsToUrls(tenant, dataItems.stream()
                .map(JsCloudParkInQo::getInCarPhoto)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList());
        dataItems.forEach(param -> {
            var inUrl = resolveImageUrl(param.getInCarPhoto(), pathToUrl);
            if (StringUtils.isBlank(inUrl)) {
                log.info("捷顺云-入场记录取图为空，跳过保存: tenant={}, itemId={}, filePath={}",
                        tenant, param.getItemId(), param.getInCarPhoto());
                return;
            }
            var inRecordReportRequest = new ParkingInRecordReportRequest();
            inRecordReportRequest.setId(param.getItemId());
            inRecordReportRequest.setTenant(tenant);
            inRecordReportRequest.setCardNo(param.getIdno());
            inRecordReportRequest.setVehicleNo(param.getCarNumber());
            inRecordReportRequest.setPlateColor(parsePlateColorFromVehicleInfo(param.getVehicleInfo()));
            inRecordReportRequest.setParkingNo(param.getParkCode());
            inRecordReportRequest.setParkingName(param.getParkName());
            inRecordReportRequest.setPassNo(param.getEquipCode());
            inRecordReportRequest.setPassName(param.getEquipName());
            inRecordReportRequest.setInUrl(inUrl);
            inRecordReportRequest.setOperator(param.getInOperator());
            if (!StringUtils.isEmpty(param.getInTime())) {
                var dateTime = param.getInTime().split(" ");
                inRecordReportRequest.setDate(dateTime[0]);
                inRecordReportRequest.setTime(dateTime.length > 1 ? dateTime[1] : "");
            }
            var parkingInRecordReportEvent = new ParkingInRecordReportEvent(this, inRecordReportRequest);
            this.applicationEventPublisher.publishEvent(parkingInRecordReportEvent);
        });
        log.info("捷顺云-入场记录延时处理完成: tenant={}, count={}", tenant, dataItems.size());
    }

    /**
     * 出场记录上报
     * 图片地址通过数据中心接口获取：先登录获取令牌，再调用图片查询接口将 filePath 转为 OSS URL
     *
     * @param dataItems 出场记录列表
     * @param tenant    租户
     *
     * @return
     */
    public JsCloudResponse reportParkingOutRecordService(List<JsCloudParkOutQo> dataItems, Integer tenant) {
        try {
            if (CollectionUtils.isEmpty(dataItems)) {
                return JsCloudResponse.builder().resultCode(1).message("出场记录数据为空").build();
            }
            var msg = JsCloudParkOutDelayMessage.builder().tenant(tenant).dataItems(new ArrayList<>(dataItems)).build();
            RocketMQContextHolder.asyncSend(JsCloudParkOutDelayMessage.TOPIC, msg, JS_CLOUD_DELAY_LEVEL_20S, payload -> {
                log.error("捷顺云-出场记录延时消息发送失败: tenant={}, count={}", tenant, dataItems.size());
            });
            log.info("捷顺云-出场记录已发送延时消息: tenant={}, count={}", tenant, dataItems.size());
            return JsCloudResponse.builder().resultCode(0).message("").build();
        } catch (Exception e) {
            log.error("捷顺云-出场记录上报，异常：", e);
            return JsCloudResponse.builder().resultCode(1).message("出场记录上报异常").build();
        }
    }

    public void handleParkingOutRecord(List<JsCloudParkOutQo> dataItems, Integer tenant) {
        if (CollectionUtils.isEmpty(dataItems)) {
            return;
        }
        var pathToUrl = resolveImagePathsToUrls(tenant, dataItems.stream()
                .map(JsCloudParkOutQo::getOutCarPhoto)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList());
        dataItems.forEach(param -> {
            var outUrl = resolveImageUrl(param.getOutCarPhoto(), pathToUrl);
            if (StringUtils.isBlank(outUrl)) {
                log.info("捷顺云-出场记录取图为空，跳过保存: tenant={}, itemId={}, filePath={}",
                        tenant, param.getItemId(), param.getOutCarPhoto());
                return;
            }
            var outRecordReportRequest = new ParkingOutRecordReportRequest();
            outRecordReportRequest.setId(param.getInRecordId());
            outRecordReportRequest.setTenant(tenant);
            outRecordReportRequest.setCardNo(param.getIdno());
            outRecordReportRequest.setVehicleNo(param.getCarNumber());
            outRecordReportRequest.setPlateColor(parsePlateColorFromVehicleInfo(param.getVehicleInfo()));
            outRecordReportRequest.setParkingNo(param.getParkCode());
            outRecordReportRequest.setParkingName(param.getParkName());
            outRecordReportRequest.setPassNo(param.getOutEquipCode());
            outRecordReportRequest.setPassName(param.getOutEquipName());
            outRecordReportRequest.setOutUrl(outUrl);
            outRecordReportRequest.setOperator(param.getOutOperator());
            outRecordReportRequest.setParkingFee(param.getSsMoney() != null ? param.getSsMoney().toString() : "0");
            outRecordReportRequest.setParkingDiscFee(param.getYhMoney() != null ? param.getYhMoney().toString() : "0");
            if (!StringUtils.isEmpty(param.getOutTime())) {
                var dateTime = param.getOutTime().split(" ");
                outRecordReportRequest.setDate(dateTime[0]);
                outRecordReportRequest.setTime(dateTime.length > 1 ? dateTime[1] : "");
            }
            var parkingOutRecordReportEvent = new ParkingOutRecordReportEvent(this, outRecordReportRequest);
            this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);
        });
        log.info("捷顺云-出场记录延时处理完成: tenant={}, count={}", tenant, dataItems.size());
    }

    /**
     * 收费记录上报（支付信息），停车时间 = 收费时间 - 入场时间（秒）
     */
    public JsCloudResponse reportParkingChargeRecordService(List<JsCloudParkChargeQo> dataItems, Integer tenant) {
        try {
            if (CollectionUtils.isEmpty(dataItems)) {
                return JsCloudResponse.builder().resultCode(1).message("收费记录数据为空").build();
            }
            dataItems.forEach(param -> {
                var payRecordReportRequest = new ParkingPayRecordReportRequest();
                payRecordReportRequest.setId(param.getParkInId());
                payRecordReportRequest.setTenant(tenant);
                payRecordReportRequest.setCardNo(param.getCardId());
                payRecordReportRequest.setVehicleNo(param.getCarNumber());
                payRecordReportRequest.setParkingNo(param.getParkCode());
                payRecordReportRequest.setParkingName(param.getParkName());
                payRecordReportRequest.setParkingFee(param.getSsMoney() != null ? param.getSsMoney().toString() : "0");
                payRecordReportRequest.setParkingDiscFee(param.getYhMoney() != null ? param.getYhMoney().toString() : "0");
                var parkingSeconds = calcParkingSeconds(param.getInTime(), param.getFeesTime());
                if (parkingSeconds > 0) {
                    payRecordReportRequest.setParkingTimes(parkingSeconds);
                }
                if (StringUtils.isNotBlank(param.getPayType())) {
                    var code = switch (param.getPayType()) {
                        case "WX", "BF_WX" -> CarSafeFeeEnum.WE_CHAT_PAY.getCode().toString();
                        case "ZFB" -> CarSafeFeeEnum.ALI_PAY.getCode().toString();
                        case "YL" -> CarSafeFeeEnum.UNION_PAY.getCode().toString();
                        case "CASH" -> CarSafeFeeEnum.CASH_PAY.getCode().toString();
                        case "JYF", "YFB", "YZF" -> CarSafeFeeEnum.OTHER_PAY.getCode().toString();
                        default -> CarSafeFeeEnum.OTHER_PAY.getCode().toString();
                    };
                    payRecordReportRequest.setPayType(code);
                }
                if (StringUtils.isNotBlank(param.getFeesTime())) {
                    var dateTime = param.getFeesTime().split(" ");
                    payRecordReportRequest.setDate(dateTime[0]);
                    payRecordReportRequest.setTime(dateTime.length > 1 ? dateTime[1] : "");
                }
                payRecordReportRequest.setOperator(param.getOperatorName());
                var parkingPayRecordReportEvent = new ParkingPayRecordReportEvent(this, payRecordReportRequest);
                this.applicationEventPublisher.publishEvent(parkingPayRecordReportEvent);
            });
            log.info("捷顺云-收费记录上报成功: tenant={}, count={}", tenant, dataItems.size());
            return JsCloudResponse.builder().resultCode(0).message("").build();
        } catch (Exception e) {
            log.error("捷顺云-收费记录上报，异常：", e);
            return JsCloudResponse.builder().resultCode(1).message("收费记录上报异常").build();
        }
    }

    /**
     * 计算停车时长（秒）= 收费时间 - 入场时间
     */
    private int calcParkingSeconds(String inTimeStr, String feesTimeStr) {
        if (StringUtils.isBlank(inTimeStr) || StringUtils.isBlank(feesTimeStr)) {
            return 0;
        }
        try {
            var inTime = DateUtils.parse(inTimeStr);
            var feesTime = DateUtils.parse(feesTimeStr);
            if (inTime == null || feesTime == null) {
                return 0;
            }
            var seconds = java.time.Duration.between(inTime, feesTime).getSeconds();
            return seconds > 0 ? (int) seconds : 0;
        } catch (Exception e) {
            log.debug("计算停车时长失败: inTime={}, feesTime={}", inTimeStr, feesTimeStr, e);
            return 0;
        }
    }

    /**
     * 通过数据中心接口将图片路径解析为 OSS URL
     * 若未配置数据中心或解析失败，返回空 Map，调用方使用原始路径
     */
    private Map<String, String> resolveImagePathsToUrls(Integer tenant, List<String> filePaths) {
        if (ListUtils.isEmpty(filePaths)) {
            return Map.of();
        }
        var config = getDataCenterConfig(tenant);
        if (config == null) {
            return Map.of();
        }
        var cacheKey = TOKEN_CACHE_KEY + tenant;
        var urls = jsCloudDataCenterClient.getImageUrls(config, cacheKey, filePaths);
        if (ListUtils.isEmpty(urls) || urls.size() != filePaths.size()) {
            return Map.of();
        }
        var pathToUrl = new LinkedHashMap<String, String>();
        for (var i = 0; i < filePaths.size(); i++) {
            pathToUrl.put(filePaths.get(i), urls.get(i));
        }
        return pathToUrl;
    }

    private String resolveImageUrl(String filePath, Map<String, String> pathToUrl) {
        if (StringUtils.isBlank(filePath) || pathToUrl == null) {
            return null;
        }
        return pathToUrl.get(filePath);
    }

    /**
     * 获取数据中心配置，若未配置 dataCenterPno/dataCenterSecret 则返回 null
     */
    private JsCloudDataCenterConfig getDataCenterConfig(Integer tenant) {
        var key = SubsystemTypeConstants.PARKING_SERVER + ":jscloud:" + tenant;
        SubsystemAdapterConfig adapterConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        if (adapterConfig == null || adapterConfig.getManufacturerJson() == null) {
            return null;
        }
        var config = JsonUtils.deserialize(JsonUtils.serialize(adapterConfig.getManufacturerJson()), JsCloudDataCenterConfig.class);
        if (config == null || StringUtils.isBlank(config.getDataCenterPno()) || StringUtils.isBlank(config.getDataCenterSecret())) {
            return null;
        }
        if (StringUtils.isBlank(config.getDataCenterBaseUrl())) {
            config.setDataCenterBaseUrl("http://jhtdc.jslife.com.cn");
        }
        return config;
    }

    /**
     * 黑白灰名单接口：method=tq.jzgk.monthCardApi.addCarBlack，严格按文档请求/响应格式
     */
    public JsCloudBwListResult addCarBlack(JsCloudBwListParam param) {
        var config = getBlacklistConfig();
        var bizContent = new JsCloudBwListBizContent(
                param.getParkCode(),
                param.getCarNo(),
                param.getCarNoColor(),
                param.getType(),
                param.getBeginTime(),
                param.getEndTime(),
                param.getIsDeleted(),
                param.getRemark()
        );
        var timestamp = System.currentTimeMillis();
        var bizContentJson = JsonUtils.serialize(bizContent);
        var signMap = new LinkedHashMap<String, String>();
        // 能力编码固定为 tq.jzgk.a003
        signMap.put("abilityCode", "tq.jzgk.a003");
        signMap.put("app_id", String.valueOf(config.getAppId()));
        signMap.put("biz_content", bizContentJson);
        signMap.put("charset", "utf-8");
        signMap.put("format", "json");
        signMap.put("method", BW_LIST_METHOD);
        signMap.put("projectCode", config.getProjectCode() != null ? config.getProjectCode() : "");
        signMap.put("sign_type", "md5");
        signMap.put("timestamp", String.valueOf(timestamp));
        String sign;
        try {
            sign = JsCloudSignUtils.sign(signMap, config.getPrivateKey());
        } catch (Exception e) {
            log.error("捷顺云-黑白灰名单签名失败", e);
            throw new ServiceException("黑白灰名单签名失败: " + e.getMessage());
        }
        var apiRequest = JsCloudBwListApiRequest.builder()
                .appId(config.getAppId())
                .method(BW_LIST_METHOD)
                .signType("md5")
                .timestamp(timestamp)
                .charset("utf-8")
                .format("json")
                .projectCode(config.getProjectCode())
                .abilityCode("tq.jzgk.a003")
                .sign(sign)
                .bizContent(bizContent)
                .build();
        log.info("请求参数：{}", JsonUtils.serialize(apiRequest));
        var url = config.getBaseUrl().replaceAll("/$", "");
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>(apiRequest, headers);
        try {
            ResponseEntity<JsCloudBwListApiResponse> responseEntity =
                    restTemplate.postForEntity(url, httpEntity, JsCloudBwListApiResponse.class);
            var body = responseEntity.getBody();
            if (body == null) {
                throw new ServiceException("捷顺云黑白灰名单接口返回为空");
            }
            // 映射为统一的三字段返回：code + msg(中文) + success
            if ("10000".equals(body.getCode())) {
                var common = JsCloudCommonErrorCode.SUCCESS;
                return new JsCloudBwListResult(common.getCode(), common.getMessageZh(), Boolean.TRUE);
            }
            var common = JsCloudCommonErrorCode.from(body.getCode(), body.getMsg());
            if (common != null) {
                return new JsCloudBwListResult(common.getCode(), common.getMessageZh(), Boolean.FALSE);
            }
            // 未知错误码，透传原始信息
            Integer codeInt;
            try {
                codeInt = body.getCode() != null ? Integer.parseInt(body.getCode()) : 0;
            } catch (NumberFormatException e) {
                codeInt = 0;
            }
            return new JsCloudBwListResult(codeInt, body.getMsg(), Boolean.FALSE);
        } catch (Exception e) {
            log.error("捷顺云-黑白灰名单接口调用异常: url={}", url, e);
            throw new ServiceException("黑白灰名单接口调用失败: " + e.getMessage());
        }
    }

    private JsCloudBlacklistConfig getBlacklistConfig() {
        var key = SubsystemTypeConstants.PARKING_SERVER + ":jscloud:" + SessionContextHolder.getRequester().getTenant();
        SubsystemAdapterConfig adapterConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        if (adapterConfig == null || adapterConfig.getManufacturerJson() == null) {
            throw new ServiceException("捷顺云黑白灰名单配置为空，请配置 parkingServer:jscloud 适配器（manufacturerJson 需包含 appId、privateKey、baseUrl、projectCode、abilityCode）");
        }
        var config = JsonUtils.deserialize(JsonUtils.serialize(adapterConfig.getManufacturerJson()), JsCloudBlacklistConfig.class);
        if (config == null || StringUtils.isBlank(config.getAppId()) || StringUtils.isBlank(config.getPrivateKey())
            || StringUtils.isBlank(config.getBaseUrl()) || StringUtils.isBlank(config.getProjectCode())) {
            throw new ServiceException("捷顺云黑白灰名单配置不完整，需包含 appId、privateKey、baseUrl、projectCode、abilityCode");
        }
        return config;
    }

    /**
     * 将 Map 按 key 插入顺序序列化为 JSON 字符串（不依赖 JsonUtils，保证 biz_content 字段顺序）
     */
    private static String mapToJsonString(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        var sb = new StringBuilder();
        sb.append('{');
        var first = true;
        for (Map.Entry<String, Object> e : map.entrySet()) {
            if (!first) {
                sb.append(',');
            }
            first = false;
            sb.append('"').append(escapeJsonString(e.getKey())).append('"').append(':');
            sb.append(toJsonValue(e.getValue()));
        }
        sb.append('}');
        return sb.toString();
    }

    private static String toJsonValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Boolean) {
            return (Boolean) value ? "true" : "false";
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof String) {
            return "\"" + escapeJsonString((String) value) + "\"";
        }
        return "\"" + escapeJsonString(String.valueOf(value)) + "\"";
    }

    private static String escapeJsonString(String s) {
        if (s == null) {
            return "";
        }
        var sb = new StringBuilder();
        for (var i = 0; i < s.length(); i++) {
            var c = s.charAt(i);
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    @Override
    public List<ParkingInRecordVO> getParkingInRecords(ParkingInRecordQueryParam param) {
        return null;
    }

    @Override
    public List<ParkingOutRecordVO> getParkingOutRecords(ParkingOutRecordQueryParam param) {
        return null;
    }

    @Override
    public List<ParkingPayRecordVO> getParkingPayRecords(ParkingPayRecordQueryParam param) {
        return null;
    }
}
