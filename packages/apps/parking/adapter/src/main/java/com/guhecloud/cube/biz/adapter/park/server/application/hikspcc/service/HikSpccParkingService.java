package com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.param.HikSpccRequestParam;
import com.guhecloud.cube.biz.adapter.park.server.application.hikspcc.param.HikSpccVehicleBottomMonitoringRecordParam;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.VehicleBottomMonitorRecordEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.VehicleBottomMonitorRecordRequest;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaofeng
 * @describe 海康 智慧监管综合平台
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("DoubleBraceInitialization")
public class HikSpccParkingService extends AbstractParkingService {
    private final SubsystemRedisUtils subsystemRedisUtils;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 调海康 智慧监管综合平台接口
     *
     * @param url  请求地址
     * @param body 请求参数
     *
     * @return 结果
     */
    private String callHikInterface(String url, String body) {
        log.info("海康 智慧监管综合平台请求url:{},请求参数:{}", url, body);
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        HikSpccRequestParam hikParam = JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), HikSpccRequestParam.class);
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

    @Override
    public List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        return Collections.emptyList();
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
        String key =
                SubsystemTypeConstants.PARKING_SERVER + ":" + "hikspcc" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 海康 智慧监管综合平台车底监测事件解析
     *
     * @param param  请求参数
     * @param tenant 租户号
     */
    public void reportVehicleBottomMonitoringRecord(Integer tenant, String param) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(param);
            JSONObject params = jsonObject.getJSONObject("params");
            JSONArray events = params.getJSONArray("events");
            events.forEach(event -> {
                JSONObject eventJson = (JSONObject) event;
                String data = eventJson.getJSONObject("data").toJSONString();
                try {
                    data = URLDecoder.decode(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    log.error("海康车底监测记录事件参数解析异常", e);
                }
                HikSpccVehicleBottomMonitoringRecordParam monitoringRecordParam = JsonUtils.deserialize(data, HikSpccVehicleBottomMonitoringRecordParam.class);
                monitoringRecordParam.setTenant(tenant);
                //进入监区
                VehicleBottomMonitorRecordRequest monitorRecordRequest = ObjectUtils.copying(monitoringRecordParam, VehicleBottomMonitorRecordRequest.class);
                if ("1".equals(monitoringRecordParam.getDirection())) {
                    monitorRecordRequest.setAccessType("ACCESS_IN");
                } else {
                    monitorRecordRequest.setAccessType("ACCESS_OUT");
                }
                if ("true".equals(monitoringRecordParam.getIsAuto())) {
                    monitorRecordRequest.setIsAuto("AUTOMATIC");
                } else {
                    monitorRecordRequest.setIsAuto("MANUAL");
                }
                monitorRecordRequest.setAccessTime(DateUtils.parse(monitoringRecordParam.getCaptureTime()));
                VehicleBottomMonitorRecordEvent monitorRecordEvent = new VehicleBottomMonitorRecordEvent(this, monitorRecordRequest);
                this.applicationEventPublisher.publishEvent(monitorRecordEvent);
            });
        } catch (Exception e) {
            log.error("海康 智慧监管综合平台出入场事件解析异常", e);
        }
    }
}
