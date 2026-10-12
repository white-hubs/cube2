package com.guhecloud.cube.srv.gen.adapter.access.control.server.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.enumeration.EventResultEnum;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.uniview.AreaInfo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.uniview.ThirdPartyDeviceBean;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.model.uniview.UniViewPage;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessDevicesParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.AccessPointsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorControlParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.service.UniViewAccessService;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.utils.uniview.FaceGateLogin;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.utils.uniview.UniData;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.utils.uniview.UniResult;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AccessPointVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.AuthDoorStatusVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.ClientHttpVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorControlResultVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorDevicesVo;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.guhecloud.platform.base.constants.DateFormat.SLASH_DATETIME_FORMAT;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UniViewAccessServiceImpl implements UniViewAccessService {
    public static final String AREAS_URL = "/fastgate/areas?conditionParam=";
    public static final String DEVICE_INFO_URL = "/fastgate/thirdpartylock/query";
    public static final String DEVICE_CONTROLS_URL = "/fastgate/thirdpartylock/deviceOpenDoor";
    public static final String EVENT_URL = "/fastgate/accessRecords?conditionParam=";
    private final FaceGateLogin faceGateLogin;
    private final RedisTemplate<String, String> redisTemplate;
    private final RestTemplate restTemplate;

    @Override
    public Page<AccessPointVo> getDoorPoints(AccessPointsParam accessPointsParam) {
        return null;
    }

    @Override
    public Page<DoorDevicesVo> getDoorDevices(AccessDevicesParam accessDevicesParam) {
        return null;
    }

    @Override
    public Page<DoorEventsVo> getDoorEvents(DoorEventsParam doorEventsParam) throws UnsupportedEncodingException {
        ClientHttpVo clientHttpVo = faceGateLogin.getConfigClient();
        //获取地址
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", DateUtils.format(DateUtils.parse(doorEventsParam.getStartTime()), SLASH_DATETIME_FORMAT));
        map.put("endTime", DateUtils.format(DateUtils.parse(doorEventsParam.getEndTime()), SLASH_DATETIME_FORMAT));
        map.put("page", doorEventsParam.getPageNo().toString());
        map.put("size", doorEventsParam.getPageSize().toString());
        String sUrl = clientHttpVo.getHost() + EVENT_URL + URLEncoder.encode(JSON.toJSONString(map), StandardCharsets.UTF_8);

        UniResult data = doGet(sUrl);
        if (data.getErrCode() == 1010) {
            // 使cookie失效
            faceGateLogin.clearCookie();
        }
        List<UniData> uniDataList = JSONArray.parseArray(JSON.toJSONString(data.getData()), UniData.class);
        List<DoorEventsVo> objects = new ArrayList<>();
        if (ListUtils.nonEmpty(uniDataList)) {
            for (UniData uniData : uniDataList) {
                DoorEventsVo doorEventsVo = new DoorEventsVo();
                doorEventsVo.setEventId(uniData.getRecordId());
                doorEventsVo.setDoorName(uniData.getDeviceName());
                switch (uniData.getModeIndex()) {
                    case "1":
                        doorEventsVo.setEventType("197162");
                        break;
                    case "16":
                        doorEventsVo.setEventType("198914");
                        break;
                    case "256":
                        doorEventsVo.setEventType("196893");
                        break;
                }
                doorEventsVo.setDoorIndexCode(uniData.getDeviceCode());
                doorEventsVo.setInAndOutType(uniData.getInorout());
                switch (uniData.getPersonType()) {
                    case 1:
                        doorEventsVo.setEventName("布控");
                        break;
                    case 2:
                        doorEventsVo.setEventName("陌生人");
                        break;
                    case 3:
                        doorEventsVo.setEventName("员工");
                        break;
                    case 4:
                        doorEventsVo.setEventName("访客");
                        break;
                }
                doorEventsVo.setEventWay(uniData.getStrInorout());
                doorEventsVo.setPersonName(uniData.getPersonName());
                doorEventsVo.setPersonId(uniData.getPersonCode());
                doorEventsVo.setEventTime(uniData.getRecordDate() + " " + uniData.getRecordTime());
                doorEventsVo.setEventResult(
                        (uniData.getPersonType() == 1 || uniData.getPersonType() == 2) ? EventResultEnum.DO_REFUSE.name()
                                                                                       : EventResultEnum.DO_PASS.name());
                objects.add(doorEventsVo);
            }
        }
        Page<DoorEventsVo> page = new Page<>();
        page.setRecords(objects);
        return page;
    }

    private UniResult doGet(String sUrl) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.COOKIE, faceGateLogin.getCookie());
        ResponseEntity<UniResult> responseEntity = restTemplate.exchange(sUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), UniResult.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        }
        throw new ServiceException("请求失败,错误码：%s", responseEntity.getStatusCode().value());
    }

    @Override
    public List<DoorControlResultVo> doorControl(DoorControlParam param) {
        List<String> doorIndexCodes = param.getDoorIndexCodes();
        Assert.isParameterError(ListUtils.isEmpty(doorIndexCodes), "返控设备编号不能为空");
        List<DoorControlResultVo> vos = new ArrayList<>();
        List<Map<String, Object>> body = new ArrayList<>();
        for (String doorIndexCode : doorIndexCodes) {
            String[] split = doorIndexCode.split("_");
            ThirdPartyDeviceBean deviceBean = queryDoorDeviceInfo(split[0]);
            if (ObjectUtils.isNotEmpty(deviceBean)) {
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("deviceIp", deviceBean.getDeviceIp());
                bodyParam.put("doorIndex", Integer.parseInt(split[1]));
                bodyParam.put("opreate", param.getControlType() == 2 ? 0 : param.getControlType());
                body.add(bodyParam);
            }
        }
        ClientHttpVo config = faceGateLogin.getConfigClient();

        UniResult data = doPost(config.getHost() + DEVICE_CONTROLS_URL, body);
        if (data.getErrCode() == 1010) {
            // 使cookie失效
            faceGateLogin.clearCookie();
        }
        log.info("宇视门禁返控制result：{}", JSON.toJSONString(data));
        vos.add(DoorControlResultVo.builder()
                        .controlResultCode(String.valueOf(data.getErrCode()))
                        .controlResultDesc(data.getErrMsg())
                        .doorIndexCode(JSON.toJSONString(param.getDoorIndexCodes()))
                        .build());
        return vos;
    }

    private <P> UniResult doPost(String url, P body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.COOKIE, faceGateLogin.getCookie());
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<P> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<UniResult> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, UniResult.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        }
        throw new ServiceException("请求失败,错误码：%s", responseEntity.getStatusCode().value());
    }

    @Override
    public List<AuthDoorStatusVo> getDoorStates(List<String> ids) {
        List<AuthDoorStatusVo> list = new ArrayList<>();
        String areaCode = queryAreaCode();
        if (ListUtils.nonEmpty(ids) && StringUtils.isNotBlank(areaCode)) {
            for (String id : ids) {
                String[] split = id.split("_");
                ThirdPartyDeviceBean deviceBean = queryDoorDeviceInfo(split[0]);
                if (ObjectUtils.isNotNull(deviceBean)) {
                    int doorState = deviceBean.getDoorList().getFirst().getDoorStatus();
                    if (split.length > 1) {
                        Map<String, Integer> collect = new HashMap<>();
                        deviceBean.getDoorList().forEach(door -> {collect.put(String.valueOf(door.getDoorIndex()), door.getDoorStatus());});
                        doorState = collect.get(split[1]);
                    }
                    list.add(AuthDoorStatusVo.builder()
                                     .doorState(doorState)
                                     .doorIndexCode(id)
                                     .build());
                }
            }
        }
        return list;
    }

    public ThirdPartyDeviceBean queryDoorDeviceInfo(String deviceCode) {
        ClientHttpVo config = faceGateLogin.getConfigClient();
        String areaCode = queryAreaCode();
        if (StringUtils.isNotBlank(areaCode)) {
            String url = DEVICE_INFO_URL + "?page=1&size=10&deviceCode=" + deviceCode + "&deviceType=1001&areaCode=" + areaCode;

            UniResult result = doGet(config.getHost() + url);
            log.debug("宇视门禁deviceInfo查询result：{}", JSON.toJSONString(result));
            if (result.getErrCode() == 1010) {
                // 使cookie失效
                faceGateLogin.clearCookie();
            }
            if (result.getErrCode() == 200) {
                UniViewPage page = JSONObject.parseObject(JSON.toJSONString(result.getData()), UniViewPage.class);
                if (ObjectUtils.isNotNull(page) && ObjectUtils.isNotNull(page.getData())) {
                    return page.getData().getFirst();
                }
            }
        }
        return null;
    }

    public String queryAreaCode() {
        String areaCode = redisTemplate.opsForValue().get("uniViewAreaCode");
        if (StringUtils.isBlank(areaCode)) {
            ClientHttpVo config = faceGateLogin.getConfigClient();
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("ParentCode", "area");
            paramMap.put("Restype", 3);
            String encode = null;
            encode = URLEncoder.encode(JSON.toJSONString(paramMap), StandardCharsets.UTF_8);
            UniResult data = doGet(config.getHost() + AREAS_URL + encode);
            log.info("宇视门禁areaCode查询result：{}", data.getErrCode());
            if (data.getErrCode() == 1010) {
                // 使cookie失效
                faceGateLogin.clearCookie();
            }
            if (data.getErrCode() == 200) {
                List<AreaInfo> areaInfos = JSONArray.parseArray(JSON.toJSONString(data.getData())).toJavaList(AreaInfo.class);
                if (ObjectUtils.isNotNull(areaInfos)) {
                    areaCode = areaInfos.getFirst().getCode();
                    redisTemplate.opsForValue().set("uniViewAreaCode", areaCode);
                }
            }
        }
        return areaCode;
    }
}
