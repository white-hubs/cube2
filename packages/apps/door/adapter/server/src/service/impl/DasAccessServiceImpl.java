package com.guhecloud.cube.srv.gen.adapter.access.control.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration.EventResultEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.das.api.config.DasConfig;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.das.api.util.DasSignatureGenerator;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.das.api.vo.DasControlResponse;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.das.api.vo.DasDoorEventInfoResponse;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessDevicesParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessPointsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorControlParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.DasAccessService;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessPointVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AuthDoorStatusVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorControlResultVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorDevicesVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectIdUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 达实门禁对接文档：
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DasAccessServiceImpl implements DasAccessService {

    /**
     * 员工门禁记录查询(人脸+卡片)
     */
    private static final String GET_DOOR_PAGE = "/api/AccessInterface/GetEmpMJRecordByAll";
    /**
     * TCP门禁远程开门
     */
    private static final String DOOR_NORMAL = "/api/AccessInterface/RemoteOpenDoor";

    private final RestTemplate restTemplate;

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public Page<AccessPointVo> getDoorPoints(AccessPointsParam accessPointsParam) {
        return null;
    }

    @Override
    public Page<DoorDevicesVo> getDoorDevices(AccessDevicesParam accessDevicesParam) {
        return null;
    }

    @Override
    public Page<DoorEventsVo> getDoorEvents(DoorEventsParam doorEventsParam) throws Exception {
        log.info("达实门禁记录采集，参数为：{}", JsonUtils.serialize(doorEventsParam));
        //获取配置
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        //刷新配置
        DasConfig dasConfig = this.getAccessConfig(subAdapterRefreshConfig);
        //组装请求
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> params = new HashMap<>();
        params.put("pageIndex", doorEventsParam.getPageNo());
        params.put("pageSize", doorEventsParam.getPageSize());
        //查询开始时间，格式：yyyy-MM-dd HH:mm:ss，不送默认从当前时间点减30天
        params.put("startTime", doorEventsParam.getStartTime());
        //查询结束时间，格式：yyyy-MM-dd HH:mm:ss ，不送默认是当前时间点
        params.put("endTime", doorEventsParam.getEndTime());
        params.put("timestamp", System.currentTimeMillis());
        //签名
        params.put("sign", DasSignatureGenerator.generateSign(params, dasConfig.getKey()));

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(params, headers);

        // 将参数拼入请求url中
        String url = dasConfig.getUrl() + GET_DOOR_PAGE;
        log.info("达实门禁记录采集参数 url:{}", url);
        ResponseEntity<DasDoorEventInfoResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, DasDoorEventInfoResponse.class);
        log.info("达实门禁记录采集，返回结果：{}", JsonUtils.serialize(responseEntity));

        if (responseEntity.getStatusCode().value() == 200) {
            DasDoorEventInfoResponse response = responseEntity.getBody();
            if (!ObjectUtils.isEmpty(response) && 0 == response.getCode() && !ObjectUtils.isEmpty(response.getData())) {
                if (com.guhecloud.platform.base.util.CollectionUtils.isNotEmpty(response.getData().getDataList())) {
                    List<DoorEventsVo> doorEventsVos = dasDoorEventConvert(response.getData().getDataList());

                    Page<DoorEventsVo> page = new Page<>();
                    page.setTotal(response.getData().getTotalCount());
                    page.setRecords(doorEventsVos);
                    return page;
                }
            }
        }
        return new Page<>();
    }

    @Override
    public List<DoorControlResultVo> doorControl(DoorControlParam param) {
        log.info("达实门禁-执行开门==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        DasConfig dasConfig = this.getAccessConfig(subAdapterRefreshConfig);
        //组装请求
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        //循环请求 操作多个
        if (com.guhecloud.platform.base.util.CollectionUtils.isNotEmpty(param.getDoorIndexCodes())) {
            List<DoorControlResultVo> resultList = new ArrayList<>();
            for (String doorId : param.getDoorIndexCodes()) {
                //body请求参数
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("doorId", doorId);
                paramMap.put("openMode", param.getOperator());
                paramMap.put("timestamp", System.currentTimeMillis());
                //签名
                paramMap.put("sign", DasSignatureGenerator.generateSign(paramMap, dasConfig.getKey()));

                HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);

                // 将参数拼入请求url中
                String url = dasConfig.getUrl() + DOOR_NORMAL;
                log.info("达实门禁-开门 url:" + url);
                ResponseEntity<DasControlResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, DasControlResponse.class);
                log.info("达实门禁-开门 返回：" + JsonUtils.serialize(responseEntity));
                if (responseEntity.getStatusCode().value() == 200) {
                    DasControlResponse response = responseEntity.getBody();
                    if (!ObjectUtils.isEmpty(response) && 0 == response.getCode()) {//业务请求成功
                        resultList.add(new DoorControlResultVo(doorId, "true", "成功"));
                    } else {
                        resultList.add(new DoorControlResultVo(doorId, "false", ObjectUtils.isEmpty(response) ? "" : response.getMsg()));
                    }
                }
            }
            return resultList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<AuthDoorStatusVo> getDoorStates(List<String> ids) {
        return Collections.emptyList();
    }

    /**
     * 获取门禁基础配置
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key = SubsystemTypeConstants.DOOR_SERVER + ":" + "das" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 获取门禁授权配置
     */
    private DasConfig getAccessConfig(SubsystemAdapterConfig config) {
        DasConfig dasConfig = JsonUtils.deserialize(config.getManufacturerJson(), DasConfig.class);
        if (dasConfig != null) {
            dasConfig.setTenant(config.getTenant());
        }
        return dasConfig;
    }

    private List<DoorEventsVo> dasDoorEventConvert(List<DasDoorEventInfoResponse.DasDoorEventInfo> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            return dataList.stream().map(r -> DoorEventsVo.builder()
                    //第三方没有提供id，这里自动生成一份（在调用方处生成更合理一些），这里由时间控制记录唯一
                    .eventId(ObjectIdUtils.next())
                    .doorIndexCode(r.getDevId())
                    .doorName(r.getDptName())
                    .eventType(r.getEventType())
                    .personId(r.getEmpNo())
                    .personName(r.getEmpName())
                    .inAndOutType(Integer.valueOf(r.getInOutFlag()))
                    .eventName(r.getEventType())
                    .eventTime(r.getRecordTime())
                    .eventWay(r.getEventType())
                    .eventResult(EventResultEnum.DO_PASS.name())
                    .build()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
