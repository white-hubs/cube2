package com.guhecloud.cube.biz.adapter.park.server.application.intercity.service;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.intercity.param.IntercityParkingInRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.intercity.param.IntercityParkingOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingInRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingOutRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingInRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingOutRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.BasicResponse;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkJsonVo;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.DigestUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IntercityParkingService extends AbstractParkingService {

    private final RestTemplate restTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        log.info("城际停车==========================================");
        String key = SubsystemTypeConstants.PARKING_SERVER + ":" + "intercity" + ":" +
                     SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        ParkJsonVo parkJsonVo = JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), ParkJsonVo.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
        JSONObject objectObjectMap = new JSONObject();
        objectObjectMap.put("parkCode", parkJsonVo.getParkCode());
        LocalDateTime dateTime = LocalDateTime.now();
        String formatDate = DateUtils.format(dateTime, "yyyy-MM-dd HH:mm:ss");
        String sign = "accessID=" + URLEncoder.encode(parkJsonVo.getAccessID(), "UTF-8") + "&apiCode=" +
                      URLEncoder.encode(parkJsonVo.getApiCode(), "UTF-8") + "&groupCode=" +
                      URLEncoder.encode(parkJsonVo.getGroupCode(), "UTF-8") + "&param=" +
                      URLEncoder.encode(objectObjectMap.toString(), "UTF-8") + "&timestamp=" + URLEncoder.encode(formatDate, "UTF-8") +
                      "&secretKey=" + parkJsonVo.getSecretKey();
        String s = DigestUtils.md5Hex(sign);
        // 将参数拼入请求url中
        String url = subAdapterRefreshConfig.getServiceAddr() + "?accessID=" + URLEncoder.encode(parkJsonVo.getAccessID(), "UTF-8") +
                     "&apiCode=" + URLEncoder.encode(parkJsonVo.getApiCode(), "UTF-8") + "&groupCode=" +
                     URLEncoder.encode(parkJsonVo.getGroupCode(), "UTF-8") + "&param=" +
                     URLEncoder.encode(objectObjectMap.toString(), "UTF-8") + "&sign=" + s + "&timestamp=" +
                     URLEncoder.encode(formatDate, "UTF-8");
        URI uri = new URI(url);
        log.info("城际uri======" + uri);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, JSONObject.class);
        JSONObject body = responseEntity.getBody();
        Object o = body.get("data");
        log.info("城际data======" + o);
        Map<String, Object> map = new ObjectMapper().convertValue(o, Map.class);
        Integer total = (Integer) map.get("total");
        Integer space = (Integer) map.get("space");
        ArrayList<ParkingInfoVO> objects = new ArrayList<>();
        ParkingInfoVO parkingInfoVO = new ParkingInfoVO();
        parkingInfoVO.setTotalNum(total);
        parkingInfoVO.setTotalSpaceNum(space);
        parkingInfoVO.setTotalInNum(total - space);
        parkingInfoVO.setCode(parkingInfoVO.getCode());
        parkingInfoVO.setName(parkingInfoVO.getName());
        objects.add(parkingInfoVO);
        return objects;
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

    public BasicResponse reportParkingInRecord(@NonNull IntercityParkingInRecordReportParam param) {
        ParkingInRecordReportRequest inRecordReportRequest = ObjectUtils.copying(param, ParkingInRecordReportRequest.class);
        String date = inRecordReportRequest.getDate();
        String[] s = date.split(" ");
        inRecordReportRequest.setDate(s[0]);
        inRecordReportRequest.setTime(s[1]);
        inRecordReportRequest.setInImg(inRecordReportRequest.getInUrl());
        ParkingInRecordReportEvent parkingInRecordReportEvent = new ParkingInRecordReportEvent(this, inRecordReportRequest);
        this.applicationEventPublisher.publishEvent(parkingInRecordReportEvent);
        return parkingInRecordReportEvent.get();
    }

    public BasicResponse reportParkingOutRecord(@NonNull IntercityParkingOutRecordReportParam param) {
        ParkingOutRecordReportRequest outRecordReportRequest = ObjectUtils.copying(param, ParkingOutRecordReportRequest.class);
        String date = outRecordReportRequest.getDate();
        String[] s = date.split(" ");
        outRecordReportRequest.setDate(s[0]);
        outRecordReportRequest.setTime(s[1]);
        ParkingOutRecordReportEvent parkingOutRecordReportEvent = new ParkingOutRecordReportEvent(this, outRecordReportRequest);
        this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);
        return parkingOutRecordReportEvent.get();
    }
}
