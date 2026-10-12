package com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.config.BsgoalConfig;
import com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.enums.BsgoalBusinessCodeEnum;
import com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.enums.BsgoalResultCode;
import com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.enums.BsgoalSingEnum;
import com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.response.BsgoalCarNumberResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.response.BsgoalParkInOutResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.response.BsgoalParkInfoResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.sdk.SignatureCompute;
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
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
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
import java.util.Objects;
import java.util.SortedMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BsgoalParkingService extends AbstractParkingService {

    private final RestTemplate restTemplate;

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public List<ParkingInfoVO> getParkingInfo() {
        log.info("博思高智慧停车-车场信息==========================================");
        List<ParkingInfoVO> parkingInfos = new ArrayList<>();
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        //获取认证配置信息
        BsgoalConfig parkingConfig = getParkingConfig(subAdapterRefreshConfig);
        //组装请求
        //获取认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        //post body参数
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("AccessCode", parkingConfig.getAccessCode());
        bodyParam.put("BusinessCode", BsgoalBusinessCodeEnum.AREA_GET_LIST.getCode());
        bodyParam.put("SignType", BsgoalSingEnum.SHA1.getCode());
        bodyParam.put("CompanyID", parkingConfig.getCompanyId());

        if (!StringUtils.isEmpty(parkingConfig.getParkingIds())) {
            String[] parkingIds = parkingConfig.getParkingIds().split(",");
            for (String parkingId : parkingIds) {
                bodyParam.put("ParkingID", parkingId);

                //加密
                SortedMap<String, String> sortedMap = SignatureCompute.sortedMap(bodyParam);
                String sign = SignatureCompute.Signature(sortedMap);

                Map<String, Object> paramMap = Maps.newHashMap(sortedMap);
                paramMap.put("Sign", sign);

                HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);

                // 将参数拼入请求url中
                String url = parkingConfig.getUrl();
                log.info("博思高请求url:" + url);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
                log.info("博思高停车场信息 返回：" + JsonUtils.serialize(responseEntity));
                ObjectMapper objectMapper = new ObjectMapper();
                BsgoalParkInfoResponse body = new BsgoalParkInfoResponse();
                try {
                    body = objectMapper.readValue(responseEntity.getBody(), BsgoalParkInfoResponse.class);
                } catch (JsonProcessingException e) {
                    log.error("博思高停车场信息 转换异常:{}", e.getMessage());
                }

                if (responseEntity.getStatusCodeValue() == 200) {//业务请求成功
                    if (BsgoalResultCode.SUCCESS.getCode() == body.getStatus()) {
                        List<BsgoalParkInfoResponse.ParkingInfo> datas = body.getData();
                        if (CollectionUtils.isNotEmpty(datas)) {
                            List<ParkingInfoVO> parkingList = datas.stream().map(parkingInfo -> {
                                ParkingInfoVO parkingInfoVO = new ParkingInfoVO();
                                parkingInfoVO.setName(parkingInfo.getAreaName());
                                //获取剩余总数（未提供批量接口，仅能一个个查询）
                                BsgoalCarNumberResponse carNumberResponse = this.getCarNumber(parkingConfig, parkingInfo.getParkingId());
                                if (carNumberResponse != null && !Objects.isNull(carNumberResponse.getData())) {
                                    parkingInfoVO.setTotalNum(carNumberResponse.getData().getTotalSpace());
                                    parkingInfoVO.setTotalSpaceNum(carNumberResponse.getData().getRemainingSpace());
                                    parkingInfoVO.setTotalInNum(carNumberResponse.getData().getPresentSpace());
                                }
                                return parkingInfoVO;
                            }).collect(Collectors.toList());
                            if (CollectionUtils.isNotEmpty(parkingList)) {
                                parkingInfos.addAll(parkingList);
                            }
                        }
                    }
                }
            }
        }
        return parkingInfos;
    }

    private BsgoalCarNumberResponse getCarNumber(BsgoalConfig parkingConfig, String parkingId) {
        //获取认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        //post body参数
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("AccessCode", parkingConfig.getAccessCode());
        bodyParam.put("BusinessCode", BsgoalBusinessCodeEnum.LOT_GET_LIST.getCode());
        bodyParam.put("SignType", BsgoalSingEnum.SHA1.getCode());
        bodyParam.put("CompanyID", parkingConfig.getCompanyId());
        bodyParam.put("ParkingID", parkingId);

        //加密
        SortedMap<String, String> sortedMap = SignatureCompute.sortedMap(bodyParam);
        String sign = SignatureCompute.Signature(sortedMap);

        Map<String, Object> paramMap = Maps.newHashMap(sortedMap);
        paramMap.put("Sign", sign);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);

        // 将参数拼入请求url中
        String url = parkingConfig.getUrl();
        log.info("博思高请求url:" + url);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        log.info("博思高停车场信息 返回：" + JsonUtils.serialize(responseEntity));
        ObjectMapper objectMapper = new ObjectMapper();
        BsgoalCarNumberResponse body = new BsgoalCarNumberResponse();
        try {
            body = objectMapper.readValue(responseEntity.getBody(), BsgoalCarNumberResponse.class);
        } catch (JsonProcessingException e) {
            log.error("博思高停车场信息 转换异常:{}", e.getMessage());
        }
        if (responseEntity.getStatusCodeValue() == 200) {//业务请求成功
            if (BsgoalResultCode.SUCCESS.getCode() == body.getStatus()) {
                return body;
            }
        }
        return null;
    }

    /**
     * 获取基础配置
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key =
                SubsystemTypeConstants.PARKING_SERVER + ":" + "bsgoal" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 获取停车场配置信息
     */
    private BsgoalConfig getParkingConfig(SubsystemAdapterConfig config) {
        return JsonUtils.deserialize(config.getManufacturerJson(), BsgoalConfig.class);
    }

    @Override
    public List<ParkingInRecordVO> getParkingInRecords(ParkingInRecordQueryParam param) {
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        //获取认证配置信息
        BsgoalConfig parkingConfig = getParkingConfig(subAdapterRefreshConfig);
        String[] parkingIds = parkingConfig.getParkingIds().split(",");
        //获取停车场入场记录
        List<BsgoalParkInOutResponse.RecordInfo> ioGetList = new ArrayList<>();
        for (String parkingId : parkingIds) {
            BsgoalParkInOutResponse bsgoalParkInOutResponse = this.getIoGetList(parkingConfig, parkingId, 1, param.getInBeginTime(), param.getInEndTime());
            if (!Objects.isNull(bsgoalParkInOutResponse) && !Objects.isNull(bsgoalParkInOutResponse.getData()) &&
                CollectionUtils.isNotEmpty(bsgoalParkInOutResponse.getData().getList())) {
                ioGetList.addAll(bsgoalParkInOutResponse.getData().getList());
            }
        }
        //上报记录
        if (CollectionUtils.isNotEmpty(ioGetList)) {
            return ioGetList.stream().map(this::recordInfo2ParkingInRecordVO).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    public List<ParkingOutRecordVO> getParkingOutRecords(ParkingOutRecordQueryParam param) {
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        //获取认证配置信息
        BsgoalConfig parkingConfig = getParkingConfig(subAdapterRefreshConfig);
        String[] parkingIds = parkingConfig.getParkingIds().split(",");
        //获取停车场出场记录
        List<BsgoalParkInOutResponse.RecordInfo> ioGetList = new ArrayList<>();
        for (String parkingId : parkingIds) {
            BsgoalParkInOutResponse bsgoalParkInOutResponse = this.getIoGetList(parkingConfig, parkingId, 2, param.getOutBeginTime(), param.getOutEndTime());
            if (!Objects.isNull(bsgoalParkInOutResponse) && !Objects.isNull(bsgoalParkInOutResponse.getData()) &&
                CollectionUtils.isNotEmpty(bsgoalParkInOutResponse.getData().getList())) {
                ioGetList.addAll(bsgoalParkInOutResponse.getData().getList());
            }
        }
        //上报记录
        if (CollectionUtils.isNotEmpty(ioGetList)) {
            return ioGetList.stream().map(this::recordInfo2ParkingOutRecordVO).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    public List<ParkingPayRecordVO> getParkingPayRecords(ParkingPayRecordQueryParam param) {
        return Collections.emptyList();
    }

    private BsgoalParkInOutResponse getIoGetList(BsgoalConfig parkingConfig, String parkingId, Integer ioFlag,
                                                 String LimitBeginTime, String LimitEndTime
                                                ) {
        //获取认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        //post body参数
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("AccessCode", parkingConfig.getAccessCode());
        bodyParam.put("BusinessCode", BsgoalBusinessCodeEnum.IO_GET_LIST.getCode());
        bodyParam.put("SignType", BsgoalSingEnum.SHA1.getCode());
        bodyParam.put("CompanyID", parkingConfig.getCompanyId());
        bodyParam.put("ParkingID", parkingId);
        bodyParam.put("IOState", 0);
        bodyParam.put("IOFlag", ioFlag);
        bodyParam.put("LimitBeginTime", LimitBeginTime);
        bodyParam.put("LimitEndTime", LimitEndTime);

        //加密
        SortedMap<String, String> sortedMap = SignatureCompute.sortedMap(bodyParam);
        String sign = SignatureCompute.Signature(sortedMap);

        Map<String, Object> paramMap = Maps.newHashMap(sortedMap);
        paramMap.put("Sign", sign);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);

        // 将参数拼入请求url中
        String url = parkingConfig.getUrl();
        log.info("博思高请求url:" + url);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
        log.info("博思高停车场信息 返回：" + JsonUtils.serialize(responseEntity));
        ObjectMapper objectMapper = new ObjectMapper();
        BsgoalParkInOutResponse body = new BsgoalParkInOutResponse();
        try {
            body = objectMapper.readValue(responseEntity.getBody(), BsgoalParkInOutResponse.class);
        } catch (JsonProcessingException e) {
            log.error("博思高停车场信息 转换异常:{}", e.getMessage());
        }
        if (responseEntity.getStatusCodeValue() == 200) {//业务请求成功
            if (BsgoalResultCode.SUCCESS.getCode() == body.getStatus()) {
                return body;
            }
        }
        return null;
    }

    private ParkingInRecordVO recordInfo2ParkingInRecordVO(BsgoalParkInOutResponse.RecordInfo recordInfo) {
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        ParkingInRecordVO parkingInRecordVO = new ParkingInRecordVO();
        parkingInRecordVO.setIdentifyId(recordInfo.getRecordID());
        parkingInRecordVO.setVehicleNo(recordInfo.getPlateNumber());
        parkingInRecordVO.setParkingNo(recordInfo.getParkingID());
        parkingInRecordVO.setChannelNo(recordInfo.getEntranceGateID());
        parkingInRecordVO.setChannelName(recordInfo.getEntranceGateName());
        parkingInRecordVO.setInTime(DateUtils.parse(recordInfo.getEntranceDate()));
        parkingInRecordVO.setVehicleType(recordInfo.getCardTypeID());
        parkingInRecordVO.setParkingInfoCode(recordInfo.getParkingID());

        parkingInRecordVO.setTenant(subAdapterRefreshConfig.getTenant());
        return parkingInRecordVO;
    }

    private ParkingOutRecordVO recordInfo2ParkingOutRecordVO(BsgoalParkInOutResponse.RecordInfo recordInfo) {
        SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();
        ParkingOutRecordVO parkingOutRecordVO = new ParkingOutRecordVO();
        parkingOutRecordVO.setIdentifyId(recordInfo.getRecordID());
        parkingOutRecordVO.setVehicleNo(recordInfo.getPlateNumber());
        parkingOutRecordVO.setParkingNo(recordInfo.getParkingID());
        parkingOutRecordVO.setChannelNo(recordInfo.getExitGateID());
        parkingOutRecordVO.setChannelName(recordInfo.getExitGateName());
        parkingOutRecordVO.setOutTime(DateUtils.parse(recordInfo.getExitDate()));
        parkingOutRecordVO.setVehicleType(recordInfo.getCardTypeID());
        parkingOutRecordVO.setParkingInfoCode(recordInfo.getParkingID());

        parkingOutRecordVO.setTenant(subAdapterRefreshConfig.getTenant());
        return parkingOutRecordVO;
    }
}
