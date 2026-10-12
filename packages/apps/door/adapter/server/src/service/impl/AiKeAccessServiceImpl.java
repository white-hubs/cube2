package com.guhecloud.cube.srv.gen.adapter.access.control.server.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration.AiKeCardTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration.AiKeDeviceTypeEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration.EventResultEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.aike.AiKePageResponse;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.aike.AiKeResponse;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.aike.Device;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.aike.Door;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.aike.DoorEvent;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessDevicesParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessPointsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorControlParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.AiKeAccessService;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessPointVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AuthDoorStatusVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.ClientHttpVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorControlResultVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorDevicesVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.Base64Utils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.SecureUtils;
import com.guhecloud.platform.base.util.StringUtils;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.constants.DateFormat.COMPACT_DATETIME_FORMAT;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AiKeAccessServiceImpl implements AiKeAccessService {
    private final SubsystemRedisUtils subsystemRedisUtils;
    private final RestTemplate restTemplate;
    //认证信息
    private String SIGN = "sign";
    private String AUTH = "Authorization";
    //获取门信息
    private String DOOR_URL = "queryDoorInfo";
    private String DOOR_DEVICE_URL = "queryUsefulExternalDeviceList";
    private String DOOR_EVENT_URL = "queryAccessRecord";
    private String DOOR_OPEN_URL = "acmsOpenDoor";

    @Override
    public Page<AccessPointVo> getDoorPoints(AccessPointsParam accessPointsParam) {
        log.info("艾科门禁-门信息查询==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        ClientHttpVo accessConfig = this.getAccessConfig(subAdapterRefreshConfig);
        //组装请求
        //获取认证信息
        Map<String, String> authInfo = this.getAuthInfo(accessConfig);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", authInfo.get(AUTH));

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(new HashMap<>(), headers);

        // 将参数拼入请求url中
        //        String url = "http://vems-test.dev.yidianting.com.cn/vems/acms/extend/api/v1/queryDoorInfo";
        String url = subAdapterRefreshConfig.getServiceAddr() + subAdapterRefreshConfig.getServiceAddrSuffix() + DOOR_URL + "?sign=" +
                     authInfo.get(SIGN);
        log.info("艾科门禁url:" + url);
        ResponseEntity<AiKeResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, AiKeResponse.class);
        log.info("艾科门禁信息 返回：" + JsonUtils.serialize(responseEntity));
        if (responseEntity.getStatusCodeValue() == 200) {
            AiKeResponse response = responseEntity.getBody();
            if (response.getSuccess()) {//业务请求成功
                List<Door> doorList = JsonUtils.deserialize(responseEntity.getBody()
                                                                    .getData(), new TypeReference<List<Door>>() {});
                if (CollectionUtils.isNotEmpty(doorList)) {
                    List<AccessPointVo> accessPointVoList = doorList.stream().map(d -> {
                        AccessPointVo accessPointVo = new AccessPointVo();
                        accessPointVo.setIndexCode(d.getDoor_id());
                        accessPointVo.setName(d.getDoor_name());
                        return accessPointVo;
                    }).collect(Collectors.toList());
                    Page<AccessPointVo> page = new Page<>();
                    page.setTotal(accessPointVoList.size());
                    page.setRecords(accessPointVoList);
                    return page;
                }
            }
        }
        return new Page<>();
    }

    @Override
    public Page<DoorDevicesVo> getDoorDevices(AccessDevicesParam accessDevicesParam) {
        log.info("艾科门禁-门禁设备信息查询==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        ClientHttpVo accessConfig = this.getAccessConfig(subAdapterRefreshConfig);
        //组装请求
        //获取认证信息
        Map<String, String> authInfo = this.getAuthInfo(accessConfig);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", authInfo.get(AUTH));

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(new HashMap<>(), headers);

        String url =
                subAdapterRefreshConfig.getServiceAddr() + subAdapterRefreshConfig.getServiceAddrSuffix() + DOOR_DEVICE_URL + "?sign=" +
                authInfo.get(SIGN);
        log.info("艾科门禁-设备url:" + url);
        ResponseEntity<AiKeResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, AiKeResponse.class);
        log.info("艾科门禁-设备信息 返回：" + JsonUtils.serialize(responseEntity));
        if (responseEntity.getStatusCodeValue() == 200) {
            AiKeResponse response = responseEntity.getBody();
            if (response.getSuccess()) {//业务请求成功
                List<Device> doorList = JsonUtils.deserialize(responseEntity.getBody()
                                                                      .getData(), new TypeReference<List<Device>>() {});
                if (CollectionUtils.isNotEmpty(doorList)) {
                    List<DoorDevicesVo> devicesVoList = doorList.stream().map(d -> {
                        DoorDevicesVo devicesVo = new DoorDevicesVo();
                        devicesVo.setIndexCode(d.getId().toString());
                        devicesVo.setName(d.getName());
                        devicesVo.setDevTypeCode(d.getType());
                        devicesVo.setDevTypeDesc(AiKeDeviceTypeEnum.valueOf(d.getType()).getDesc());
                        devicesVo.setDeviceCode(d.getId().toString());
                        devicesVo.setManufacturer(d.getBrand());
                        devicesVo.setRegionName(d.getArea_name());
                        return devicesVo;
                    }).collect(Collectors.toList());
                    Page<DoorDevicesVo> page = new Page<>();
                    page.setTotal(devicesVoList.size());
                    page.setRecords(devicesVoList);
                    return page;
                }
            }
        }
        return new Page<>();
    }

    @Override
    public Page<DoorEventsVo> getDoorEvents(DoorEventsParam doorEventsParam) throws Exception {
        log.info("艾科门禁-门禁事件信息查询==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        ClientHttpVo accessConfig = this.getAccessConfig(subAdapterRefreshConfig);
        //组装请求
        //获取认证信息
        Map<String, String> authInfo = this.getAuthInfo(accessConfig);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", authInfo.get(AUTH));
        //body请求参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("start_time", doorEventsParam.getStartTime());
        paramMap.put("end_time", doorEventsParam.getEndTime());
        paramMap.put("page_number", doorEventsParam.getPageNo());
        paramMap.put("page_size", doorEventsParam.getPageSize());
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);

        String url = subAdapterRefreshConfig.getServiceAddr() + subAdapterRefreshConfig.getServiceAddrSuffix() + DOOR_EVENT_URL + "?sign=" +
                     authInfo.get(SIGN);
        log.info("艾科门禁-门禁事件 url:" + url);
        ResponseEntity<AiKePageResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, AiKePageResponse.class);
        log.info("艾科门禁-门禁事件 返回：" + JsonUtils.serialize(responseEntity));
        if (responseEntity.getStatusCodeValue() == 200) {
            AiKePageResponse response = responseEntity.getBody();
            if (response.getSuccess()) {//业务请求成功
                AiKePageResponse.Page aikePage = JsonUtils.deserialize(responseEntity.getBody()
                                                                               .getData(), AiKePageResponse.Page.class);
                if (CollectionUtils.isNotEmpty(aikePage.getList())) {
                    List<DoorEventsVo> devicesVoList = aikePage.getList().stream().map(d -> {
                        DoorEvent event = JSONObject.parseObject(d.toJSONString(), DoorEvent.class);
                        DoorEventsVo eventsVo = new DoorEventsVo();
                        eventsVo.setEventId(IdWorker.getIdStr());
                        String eventDesc = "未知方式开门";
                        if (StringUtils.isNotBlank(event.getCard_type())) {
                            eventDesc = AiKeCardTypeEnum.valueOf("CARD_" + event.getCard_type()).getDesc();
                        }
                        eventsVo.setEventName(eventDesc + "开门");
                        eventsVo.setEventType(eventDesc);
                        eventsVo.setEventWay(eventDesc);
                        eventsVo.setEventTime(event.getAccess_time());
                        //通行结果
                        String eventResult =
                                event.getAccess_status() != null && event.getAccess_status() == 1 ? EventResultEnum.DO_PASS.name()
                                                                                                  : EventResultEnum.DO_REFUSE.name();
                        eventsVo.setEventResult(eventResult);
                        eventsVo.setCardNo(event.getCard_no());
                        eventsVo.setPersonName(event.getPersonnel_name());
                        eventsVo.setDoorName(event.getDoor_name());
                        eventsVo.setDoorIndexCode(event.getDoor_id() != null ? event.getDoor_id().toString() : "");
                        return eventsVo;
                    }).collect(Collectors.toList());
                    Page<DoorEventsVo> page = new Page<>();
                    page.setTotal(devicesVoList.size());
                    page.setRecords(devicesVoList);
                    return page;
                }
            }
        }
        return new Page<>();
    }

    @Override
    public List<DoorControlResultVo> doorControl(DoorControlParam param) {
        log.info("艾科门禁-执行开门==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        ClientHttpVo accessConfig = this.getAccessConfig(subAdapterRefreshConfig);
        //组装请求
        //获取认证信息
        Map<String, String> authInfo = this.getAuthInfo(accessConfig);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", authInfo.get(AUTH));
        //循环请求 操作多个
        if (CollectionUtils.isNotEmpty(param.getDoorIndexCodes())) {
            List<DoorControlResultVo> resultList = new ArrayList<>();
            for (String doorId : param.getDoorIndexCodes()) {
                //body请求参数
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("door_id", doorId);
                paramMap.put("operator", param.getOperator());
                HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);

                String url = subAdapterRefreshConfig.getServiceAddr() + subAdapterRefreshConfig.getServiceAddrSuffix() + DOOR_OPEN_URL +
                             "?sign=" + authInfo.get(SIGN);
                log.info("艾科门禁-开门 url:" + url);
                ResponseEntity<AiKeResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, AiKeResponse.class);
                log.info("艾科门禁-开门 返回：" + JsonUtils.serialize(responseEntity));
                if (responseEntity.getStatusCodeValue() == 200) {
                    AiKeResponse response = responseEntity.getBody();
                    if (response.getSuccess()) {//业务请求成功
                        resultList.add(new DoorControlResultVo(doorId, "true", "成功"));
                    } else {
                        resultList.add(new DoorControlResultVo(doorId, "false", response.getMessage()));
                    }
                }
            }
            return resultList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<AuthDoorStatusVo> getDoorStates(List<String> ids) {
        //目前艾科提供的接口中，没有查询门状态的接口
        //只有门禁设备状态的接口，该接口中有设备对应的门列表，可以处理成门的在线/离线状态
        log.info("艾科门禁-门禁设备状态信息查询==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        ClientHttpVo accessConfig = this.getAccessConfig(subAdapterRefreshConfig);
        //组装请求
        //获取认证信息
        Map<String, String> authInfo = this.getAuthInfo(accessConfig);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", authInfo.get(AUTH));

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(new HashMap<>(), headers);

        String url =
                subAdapterRefreshConfig.getServiceAddr() + subAdapterRefreshConfig.getServiceAddrSuffix() + DOOR_DEVICE_URL + "?sign=" +
                authInfo.get(SIGN);
        log.info("艾科门禁-设备状态url:" + url);
        ResponseEntity<AiKeResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, AiKeResponse.class);
        log.info("艾科门禁-设备状态信息 返回：" + JsonUtils.serialize(responseEntity));
        if (responseEntity.getStatusCodeValue() == 200) {
            AiKeResponse response = responseEntity.getBody();
            if (response.getSuccess()) {//业务请求成功
                List<Device> deviceList = JsonUtils.deserialize(responseEntity.getBody()
                                                                        .getData(), new TypeReference<List<Device>>() {});
                if (CollectionUtils.isNotEmpty(deviceList)) {
                    List<AuthDoorStatusVo> deviceStatusList = deviceList.stream().map(d -> {
                        List<String> doorIdList = d.getDoor_id_list();
                        if (CollectionUtils.isNotEmpty(doorIdList)) {
                            return doorIdList.stream().map(doorId -> new AuthDoorStatusVo(doorId, d.getOnline_status()))
                                    .collect(Collectors.toList());
                        } else {
                            return null;
                        }
                    }).filter(Objects::nonNull).flatMap(list -> list.stream()).collect(Collectors.toList());
                    return deviceStatusList;
                }
            }
            throw new ServiceException(
                    "艾科门禁-设备状态请求异常，业务状态：" + response.getSuccess() + " 异常信息：" + response.getMessage());
        } else {
            throw new ServiceException("艾科门禁-设备状态请求异常，状态码：" + responseEntity.getStatusCode().value());
        }
    }

    /**
     * 获取门禁基础配置
     *
     * @return
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key = SubsystemTypeConstants.DOOR_SERVER + ":" + "aike" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 获取门禁授权配置
     *
     * @param config
     *
     * @return
     */
    private ClientHttpVo getAccessConfig(SubsystemAdapterConfig config) {
        return JsonUtils.deserialize(config.getManufacturerJson(), ClientHttpVo.class);
    }

    /**
     * 获取认证相关信息
     *
     * @param accessConfig
     *
     * @return
     */
    private Map<String, String> getAuthInfo(ClientHttpVo accessConfig) {
        Map<String, String> authMap = new HashMap<>();
        String dateTimeStr = DateUtils.format(LocalDateTime.now(), COMPACT_DATETIME_FORMAT);
        //生成请求须要的sign；sign=MD5加密(key+冒号+时间+冒号+密码)，加密后的值应转换为32位小写字符串
        String sign = accessConfig.getAppKey() + ":" + dateTimeStr + ":" + accessConfig.getAppSecret();
        //md5加密 在转换成小写
        authMap.put(SIGN, SecureUtils.md5(sign).toLowerCase());

        //生成头部认证； 使用Base64编码（key + 冒号 + 时间戳）
        String auth = accessConfig.getAppKey() + ":" + dateTimeStr;
        authMap.put(AUTH, Base64Utils.encode(auth));

        return authMap;
    }
}
