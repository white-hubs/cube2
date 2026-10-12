package com.guhecloud.cube.biz.adapter.park.server.application.hikvision.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.hikvision.model.OutResult;
import com.guhecloud.cube.biz.adapter.park.server.application.hikvision.param.HikParkingInRecordParam;
import com.guhecloud.cube.biz.adapter.park.server.application.hikvision.param.HikParkingOutRecordParam;
import com.guhecloud.cube.biz.adapter.park.server.application.hikvision.param.HikParkingPlaceInRecordParam;
import com.guhecloud.cube.biz.adapter.park.server.application.hikvision.param.HikRequestParam;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingInRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingOutRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingPayRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingPlaceInRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingInRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingOutRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingPayRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingPlaceRecordRequest;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 海康接口文档: <a href="https://open.hikvision.com/#home">...</a>
 * @date 2024/3/5 11:50
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("DoubleBraceInitialization")
public class HikParkingService extends AbstractParkingService {
    private final SubsystemRedisUtils subsystemRedisUtils;
    private final ApplicationEventPublisher applicationEventPublisher;
    /**
     * 车辆进入事件
     */
    public static final int IN_EVENT = 771760131;
    /**
     * 车辆离开事件
     */
    public static final int OUT_EVENT = 771760134;
    /**
     * 车位停车事件
     */
    public static final int PARKING_SPACE_IN = 771764226;

    @Override
    public List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        JSONObject jsonBody = new JSONObject();
        String result = callHikInterface("/api/pms/v1/park/remainSpaceNum", jsonBody.toJSONString());
        log.info("海康停车场信息 返回：{}", result);
        if (StringUtils.isNotBlank(result)) {
            JSONObject responseBody = JSONObject.parseObject(result);
            if ("0".equals(responseBody.getString("code"))) {
                JSONArray parkingList = responseBody.getJSONArray("data");
                if (CollectionUtils.isNotEmpty(parkingList)) {
                    return parkingList.stream().map(json -> {
                        JSONObject data = (JSONObject) json;
                        ParkingInfoVO parkingInfoVO = new ParkingInfoVO();
                        parkingInfoVO.setTotalNum(data.getInteger("totalPlace"));
                        parkingInfoVO.setTotalSpaceNum(data.getInteger("leftPlace"));
                        parkingInfoVO.setTotalInNum(parkingInfoVO.getTotalNum() - parkingInfoVO.getTotalSpaceNum());
                        parkingInfoVO.setCode(data.getString("parkSyscode"));
                        parkingInfoVO.setName(data.getString("parkName"));
                        return parkingInfoVO;
                    }).collect(Collectors.toList());
                }
            }
        }
        return Collections.emptyList();
    }

    /**
     * 调海康接口
     *
     * @param url  请求地址
     * @param body 请求参数
     *
     * @return 结果
     */
    private String callHikInterface(String url, String body) {
        log.info("海康请求url:{},请求参数:{}", url, body);
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        HikRequestParam hikParam = JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), HikRequestParam.class);
        ArtemisConfig.host = hikParam.getHost();
        ArtemisConfig.appKey = hikParam.getAppKey();
        ArtemisConfig.appSecret = hikParam.getAppSecret();
        final String ARTEMIS_PATH = "/artemis";
        final String previewURLsApi = ARTEMIS_PATH + url;
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                //根据现场环境部署确认是http还是https
                put("https://", previewURLsApi);
            }
        };
        String contentType = MediaType.APPLICATION_JSON_VALUE;
        return ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType, null);
    }

    public void hikEventSubscribe() {
        log.info("执行海康事件订阅");
        SubsystemAdapterConfig basicConfig = this.getBasicConfig();
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("eventTypes", Arrays.asList(IN_EVENT, OUT_EVENT, PARKING_SPACE_IN));
        jsonBody.put("eventDest", basicConfig.getServiceAddr() + "/report/" + basicConfig.getTenant() + "/ParkingOutInRecord");
        String result = callHikInterface("/api/eventService/v1/eventSubscriptionByEventTypes", jsonBody.toJSONString());
        log.info("海康事件订阅返回:{}", result);
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

    /**
     * 获取基础配置
     *
     * @return 基础配置
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key = SubsystemTypeConstants.PARKING_SERVER + ":" + "hik" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 海康出入场事件解析
     *
     * @param param  请求参数
     * @param tenant 租户号
     */
    public void reportParkingOutInRecord(Integer tenant, String param) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(param);
            JSONObject params = jsonObject.getJSONObject("params");
            JSONArray events = params.getJSONArray("events");
            events.forEach(event -> {
                JSONObject eventJson = (JSONObject) event;
                Integer eventType = (Integer) eventJson.get("eventType");
                String data = eventJson.getJSONObject("data").toJSONString();
                try {
                    data = URLDecoder.decode(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    log.error("海康出入场事件参数解析异常", e);
                }
                if (IN_EVENT == eventType) {
                    //推送入记录
                    publishInRecord(tenant, data);
                } else if (OUT_EVENT == eventType) {
                    //推送出、支付记录
                    publicOutAndPayRecord(tenant, data);
                } else if (PARKING_SPACE_IN == eventType) {
                    String eventId = eventJson.getString("eventId");
                    //推送车位停车停放记录
                    publishParkingPlaceRecord(tenant, data, eventId);
                }
            });
        } catch (Exception e) {
            log.error("海康出入场事件解析异常", e);
        }
    }

    private void publicOutAndPayRecord(Integer tenant, String data) {
        //出场记录
        HikParkingOutRecordParam outRecordParam = JsonUtils.deserialize(data, HikParkingOutRecordParam.class);
        outRecordParam.setTenant(tenant);
        ParkingOutRecordReportRequest outRecordReportRequest = ObjectUtils.copying(outRecordParam, ParkingOutRecordReportRequest.class);
        OffsetDateTime parse = OffsetDateTime.parse(outRecordParam.getTime().replace(" ", "+"));
        outRecordReportRequest.setDate(StringUtils.join("-", parse.getYear(), parse.getMonthValue(), parse.getDayOfMonth()));
        outRecordReportRequest.setTime(StringUtils.join(":", parse.getHour(), parse.getMinute(), parse.getSecond()));
        ParkingOutRecordReportEvent parkingOutRecordReportEvent = new ParkingOutRecordReportEvent(this, outRecordReportRequest);
        this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);

        //缴费记录
        ParkingPayRecordReportRequest payRecordReportRequest = ObjectUtils.copying(outRecordParam, ParkingPayRecordReportRequest.class);
        OutResult outResult = outRecordParam.getOutResult();
        if (ObjectUtils.isNotEmpty(outResult)) {
            float realCost = outResult.getRealCost();
            float totalCost = outResult.getTotalCost();
            payRecordReportRequest.setParkingFee(Float.toString(realCost));
            payRecordReportRequest.setParkingDiscFee(Float.toString(totalCost - realCost));
        }
        ParkingPayRecordReportEvent parkingPayRecordReportEvent = new ParkingPayRecordReportEvent(this, payRecordReportRequest);
        this.applicationEventPublisher.publishEvent(parkingPayRecordReportEvent);
    }

    private void publishInRecord(Integer tenant, String data) {
        HikParkingInRecordParam inRecordParam = JsonUtils.deserialize(data, HikParkingInRecordParam.class);
        inRecordParam.setTenant(tenant);
        ParkingInRecordReportRequest inRecordReportRequest = ObjectUtils.copying(inRecordParam, ParkingInRecordReportRequest.class);
        OffsetDateTime parse = OffsetDateTime.parse(inRecordParam.getTime().replace(" ", "+"));
        inRecordReportRequest.setDate(StringUtils.join("-", parse.getYear(), parse.getMonthValue(), parse.getDayOfMonth()));
        inRecordReportRequest.setTime(StringUtils.join(":", parse.getHour(), parse.getMinute(), parse.getSecond()));
        ParkingInRecordReportEvent parkingInRecordReportEvent = new ParkingInRecordReportEvent(this, inRecordReportRequest);
        this.applicationEventPublisher.publishEvent(parkingInRecordReportEvent);
    }

    private void publishParkingPlaceRecord(Integer tenant, String data, String eventId) {
        HikParkingPlaceInRecordParam placeInRecordParam = JsonUtils.deserialize(data, HikParkingPlaceInRecordParam.class);
        ParkingPlaceRecordRequest placeRecordRequest = ParkingPlaceRecordRequest.builder()
                .identifyId(eventId)
                .cameraId(placeInRecordParam.getCameraId())
                .occurrenceTime(DateUtils.parse(placeInRecordParam.getEventTime()))
                .floorId(placeInRecordParam.getFloorId())
                .type("ACCESS_IN")
                .parkingId(placeInRecordParam.getParkingId())
                .picUrl(placeInRecordParam.getPicUrl())
                .placeId(placeInRecordParam.getPlaceId())
                .vehicleNo(placeInRecordParam.getPlateNo())
                .tenant(tenant)
                .build();
        this.applicationEventPublisher.publishEvent(new ParkingPlaceInRecordReportEvent(this, placeRecordRequest));
    }
}
