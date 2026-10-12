package com.guhecloud.cube.biz.adapter.park.server.application.ketuo.service;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.ketuo.param.KeTuoParkingInRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.ketuo.param.KeTuoParkingOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.ketuo.utils.SignUtils;
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
import com.guhecloud.platform.base.util.ObjectIdUtils;
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
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeTuoParkingService extends AbstractParkingService {
    private final SubsystemRedisUtils subsystemRedisUtils;

    private final RestTemplate restTemplate;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException {

        log.info("科拓停车==========================================");
        String key =
                SubsystemTypeConstants.PARKING_SERVER + ":" + "ketuo" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        ParkJsonVo parkJsonVo = JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), ParkJsonVo.class);
        HttpHeaders headers = new HttpHeaders();
        long time = DateUtils.currentTimestamp();
        String nextId = ObjectIdUtils.next();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("version", parkJsonVo.getVersion());
        JSONObject objectObjectMap = new JSONObject();
        objectObjectMap.put("parkId", parkJsonVo.getParkCode());
        objectObjectMap.put("serviceCode", "getFreeSpaceNum");
        objectObjectMap.put("ts", time);
        objectObjectMap.put("reqId", nextId);
        objectObjectMap.put("isFindCarSys", 0);
        String signKey = SignUtils.paramsSign(objectObjectMap, parkJsonVo.getSecretKey());
        objectObjectMap.put("appId", parkJsonVo.getGroupCode());
        objectObjectMap.put("key", signKey);

        // 将参数拼入请求url中
        String url = subAdapterRefreshConfig.getServiceAddr() + "/api/wec/GetFreeSpaceNum" + "?parkId=" +
                     URLEncoder.encode(parkJsonVo.getParkCode(), "UTF-8") + "&serviceCode=" +
                     URLEncoder.encode("getFreeSpaceNum", "UTF-8") + "&ts=" + URLEncoder.encode(String.valueOf(time), "UTF-8") + "&key=" +
                     URLEncoder.encode(signKey, "UTF-8") + "&reqId=" + URLEncoder.encode(nextId, "UTF-8") + "&isFindCarSys=" +
                     URLEncoder.encode(String.valueOf(0), "UTF-8") + "&appId=" + URLEncoder.encode(parkJsonVo.getGroupCode(), "UTF-8");
        URI uri = new URI(url);
        log.info("科拓停车uri======" + uri);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(objectObjectMap, headers);

        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, JSONObject.class);
        JSONObject body = responseEntity.getBody();
        Object o = body.get("data");
        log.info("科拓停车data======" + o);
        String s = o.toString();
        JSONObject jsonObject = JSONObject.parseObject(s);
        Integer total = (Integer) jsonObject.get("totalNum");
        Integer space = (Integer) jsonObject.get("freeSpaceNum");
        ArrayList<ParkingInfoVO> objects = new ArrayList<>();
        ParkingInfoVO parkingInfoVO = new ParkingInfoVO();
        parkingInfoVO.setTotalNum(total);
        parkingInfoVO.setTotalSpaceNum(space);
        parkingInfoVO.setTotalInNum(total - space);
        parkingInfoVO.setCode(jsonObject.get("parkId").toString());
        parkingInfoVO.setName(jsonObject.get("parkName").toString());
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

    public BasicResponse reportParkingInRecord(@NonNull KeTuoParkingInRecordReportParam param) {
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

    public BasicResponse reportParkingOutRecord(@NonNull KeTuoParkingOutRecordReportParam param) {
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
