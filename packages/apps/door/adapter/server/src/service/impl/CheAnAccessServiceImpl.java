package com.guhecloud.cube.srv.gen.adapter.access.control.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration.EventResultEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.chean.CheAnDoorEventQuery;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.chean.CheAnDoorOpenQuery;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.chean.CheAnQueryModel;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.chean.CheanDoorEvent;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.chean.CheanDoorOpenResponse;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.chean.CheanDoorStatus;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.chean.CheanResponse;
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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.guhecloud.platform.base.constants.DateFormat.DEFAULT_DATETIME_FORMAT;

/**
 * 车安科技-门禁
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheAnAccessServiceImpl implements AiKeAccessService {
    private final SubsystemRedisUtils subsystemRedisUtils;
    private final RestTemplate restTemplate;
    private String SHA1_PRIVATE_KEY = "GHY123456";
    //认证信息
    private String SIGN = "sign";
    private String TIMESTAMP = "timestamp";
    private String FROM = "from";
    private String NONCE = "nonce";
    //查询参数
    private String DATA = "data";
    private String DOOR_STATUS_URL = "api.aspx/acss.door.state.list";
    private String DOOR_EVENT_URL = "api.aspx/acss.iorecords.get";
    private String DOOR_OPEN_URL = "api.aspx/acss.door.open.directly";

    @Override
    public Page<AccessPointVo> getDoorPoints(AccessPointsParam accessPointsParam) {
        return new Page<>();
    }

    @Override
    public Page<DoorDevicesVo> getDoorDevices(AccessDevicesParam accessDevicesParam) {
        return new Page<>();
    }

    @Override
    public Page<DoorEventsVo> getDoorEvents(DoorEventsParam doorEventsParam) throws Exception {
        log.info("车安门禁-门禁事件信息查询==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        ClientHttpVo accessConfig = this.getAccessConfig(subAdapterRefreshConfig);
        //获取认证信息和查询条件
        CheAnQueryModel queryModel = CheAnDoorEventQuery.builder()
                .ioTimeStart(doorEventsParam.getStartTime())
                .ioTimeEnd(doorEventsParam.getEndTime()).build();

        Map<String, Object> authAndQuery = this.getAuthInfoAndQuery(accessConfig, queryModel);
        //请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        //body请求参数
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(authAndQuery, headers);

        // 将参数拼入请求url中
        String url = subAdapterRefreshConfig.getServiceAddr() + DOOR_EVENT_URL;
        log.info("车安门禁-门禁事件 url:{}", url);
        ResponseEntity<CheanResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, CheanResponse.class);
        log.info("车安门禁-门禁事件 返回：{}", JsonUtils.serialize(responseEntity));
        if (responseEntity.getStatusCodeValue() == 200) {
            CheanResponse response = responseEntity.getBody();
            if (response.getStatus() && response.getCode() == 200) {//业务请求成功
                List<CheanDoorEvent> eventList = JsonUtils.deserialize(responseEntity.getBody()
                                                                               .getData(), new TypeReference<List<CheanDoorEvent>>() {});
                if (CollectionUtils.isNotEmpty(eventList)) {
                    List<DoorEventsVo> devicesVoList = eventList.stream().map(event -> {
                        DoorEventsVo eventsVo = new DoorEventsVo();
                        eventsVo.setEventId(IdWorker.getIdStr());
                        String eventDesc = "未知方式开门";
                        if (StringUtils.isNotBlank(event.getIoTypeName())) {
                            eventDesc = event.getIoTypeName();
                        }
                        eventsVo.setEventName(eventDesc);
                        eventsVo.setEventType(eventDesc);
                        eventsVo.setEventWay(eventDesc);
                        eventsVo.setEventTime(event.getIoTime());
                        //通行结果 目前数据中无法判断是否成功或是失败 默认成功；待现场联调时，根据情况而定
                        String eventResult = EventResultEnum.DO_PASS.name();
                        eventsVo.setEventResult(eventResult);
                        eventsVo.setCardNo(event.getCardNo());
                        eventsVo.setPersonName(event.getNumNo());//没有实际的人名，用卡号代替
                        eventsVo.setDoorName(event.getRoomName());
                        eventsVo.setDoorIndexCode(event.getDoorNo());
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
        log.info("车安门禁-执行开门==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        ClientHttpVo accessConfig = this.getAccessConfig(subAdapterRefreshConfig);
        //获取认证和控制信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        //循环请求 操作多个
        if (CollectionUtils.isNotEmpty(param.getDoorIndexCodes())) {
            List<DoorControlResultVo> resultList = new ArrayList<>();
            for (String doorId : param.getDoorIndexCodes()) {
                String[] doorInfo = doorId.split("_");//需要保证填的三方ID为：控制板编号+"_"+门编号
                //body请求参数
                CheAnDoorOpenQuery queryModel = CheAnDoorOpenQuery.builder()
                        .controllerNo(doorInfo[0])//控制板编号
                        .doorNo(doorInfo[1])//门编号
                        .build();
                Map<String, Object> authAndParam = this.getAuthInfoAndQuery(accessConfig, queryModel);
                HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(authAndParam, headers);

                // 将参数拼入请求url中
                String url = subAdapterRefreshConfig.getServiceAddr() + DOOR_OPEN_URL;
                log.info("车安门禁-开门 url:{}", url);
                ResponseEntity<CheanResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, CheanResponse.class);
                log.info("车安门禁-开门 返回：{}", JsonUtils.serialize(responseEntity));
                if (responseEntity.getStatusCodeValue() == 200) {
                    CheanDoorOpenResponse response = JsonUtils.deserialize(responseEntity.getBody()
                                                                                   .getData(), new TypeReference<CheanDoorOpenResponse>() {});
                    if ("1".equals(response.getFlag())) {//业务请求成功
                        resultList.add(new DoorControlResultVo(doorId, "true", "成功"));
                    } else {
                        resultList.add(new DoorControlResultVo(doorId, "false", response.getMemo()));
                    }
                }
            }
            return resultList;
        }
        return Collections.emptyList();
    }

    @Override
    public List<AuthDoorStatusVo> getDoorStates(List<String> ids) {
        log.info("车安门禁-门禁设备状态信息查询==========================================");
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        ClientHttpVo accessConfig = this.getAccessConfig(subAdapterRefreshConfig);

        //获取认证信息
        Map<String, Object> authAndQuery = this.getAuthInfoAndQuery(accessConfig, null);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(authAndQuery, headers);

        // 将参数拼入请求url中
        String url = subAdapterRefreshConfig.getServiceAddr() + DOOR_STATUS_URL;
        log.info("车安门禁-设备状态url:{}", url);
        ResponseEntity<CheanResponse> responseEntity = restTemplate.postForEntity(url, httpEntity, CheanResponse.class);
        log.info("车安门禁-设备状态信息 返回：{}", JsonUtils.serialize(responseEntity));
        if (responseEntity.getStatusCodeValue() == 200) {
            CheanResponse response = responseEntity.getBody();
            if (response.getStatus() && response.getCode() == 200) {//业务请求成功
                List<CheanDoorStatus> doorStatusList = JsonUtils.deserialize(responseEntity.getBody()
                                                                                     .getData(), new TypeReference<List<CheanDoorStatus>>() {});
                if (CollectionUtils.isNotEmpty(doorStatusList)) {
                    return doorStatusList.stream().map(d ->
                                                               new AuthDoorStatusVo(d.getDoorNo(), Integer.valueOf(d.getOpened()))
                                                      ).collect(Collectors.toList());
                }
            }
            throw new ServiceException("车安门禁-门状态请求异常，业务状态：" + response.getCode() + " 异常信息：" + response.getMessage());
        } else {
            throw new ServiceException("车安门禁-门状态请求异常，状态码：" + responseEntity.getStatusCode().value());
        }
    }

    /**
     * 获取门禁基础配置
     *
     * @return
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key = SubsystemTypeConstants.DOOR_SERVER + ":" + "chean" + ":" + SessionContextHolder.getRequester().getTenant().toString();
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
    private Map<String, Object> getAuthInfoAndQuery(ClientHttpVo accessConfig, CheAnQueryModel queryModel) {
        Map<String, Object> authMapAndQuery = new HashMap<>();
        //时间
        String timestamp = DateUtils.format(LocalDateTime.now(), DEFAULT_DATETIME_FORMAT);
        //随机数
        String nonce = ThreadLocalRandom.current().nextInt(100) + ""; // 生成 0 到 99 的随机整数
        //用户标识
        String from = accessConfig.getAccount();
        //待加密字符串
        String signStr = timestamp + nonce + from;
        if (queryModel != null) {
            signStr = signStr + JsonUtils.serialize(queryModel);
            authMapAndQuery.put(DATA, queryModel);
        }
        log.info("车安门禁，签名参数：{}", signStr);
        String sign = this.calculateHMAC(signStr, SHA1_PRIVATE_KEY);

        authMapAndQuery.put(FROM, from);
        authMapAndQuery.put(SIGN, sign);
        authMapAndQuery.put(NONCE, nonce);
        authMapAndQuery.put(TIMESTAMP, timestamp);
        authMapAndQuery.put("branchno", "1");
        authMapAndQuery.put("queryFormat", null);
        log.info("车安门禁，详细参数：{}", JsonUtils.serialize(authMapAndQuery));
        return authMapAndQuery;
    }

    public String calculateHMAC(String data, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKeySpec);
            byte[] bytes = mac.doFinal(data.getBytes());
            return Base64Utils.encode(bytes);
        } catch (Exception e) {
            log.error("车安门禁，加密失败", e);
            // 处理异常
            return "";
        }
    }
}
