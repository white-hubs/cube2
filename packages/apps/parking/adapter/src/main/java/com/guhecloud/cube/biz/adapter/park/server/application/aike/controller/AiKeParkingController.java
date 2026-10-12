package com.guhecloud.cube.biz.adapter.park.server.application.aike.controller;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.model.AiKe2Response;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.param.AiKeParking2FeeRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.param.AiKeParking2InRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.param.AiKeParking2OutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.param.AiKeParkingV6FeeRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.aike.service.AiKeParking2Service;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.platform.base.util.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 艾科智慧停车场接口
 * 使用项目 南沙
 *
 * @author appet
 * @date 2023/10/11 16:45
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AiKeParkingController {
    private final AiKeParking2Service aiKeParkingService;

    /**
     * 获取车场信息
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/aike/parking-info")
    public List<ParkingInfoVO> getIntercityParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        return this.aiKeParkingService.getParkingInfo();
    }

    /**
     * 车场数据订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/aike/parking-subscribe")
    public Boolean subscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.aiKeParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/aike/parking-un-subscribe")
    public Boolean unSubscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.aiKeParkingService.parkingUnSubscribe(param);
    }

    /**
     * 上报车场入场记录
     *
     * @return
     */
    @PostMapping("/public/aike/parking2-in-record/{tenant}/reportCarIn")
    public AiKe2Response reportParking2InRecord(@PathVariable Integer tenant,
                                                @RequestBody String reportData,
                                                HttpServletRequest request
                                               ) {
        String ipAddr = request.getRemoteAddr();
        log.info("艾科停车场，进场调用者IP地址：{}", ipAddr);
        log.info("艾科停车场，进场上报：{}", reportData);
        JSONObject inRecordJson = JSONObject.parseObject(reportData).getJSONObject("biz_content");
        String carInRecord = inRecordJson.toJSONString();
        try {
            carInRecord = URLDecoder.decode(carInRecord, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("艾科停车场，进场上报,解码异常", e);
        }
        AiKeParking2InRecordReportParam param = JsonUtils.deserialize(carInRecord, AiKeParking2InRecordReportParam.class);
        param.setTenant(tenant);
        param.setInUrl("http://" + ipAddr + ":8092" + param.getInUrl());
        return this.aiKeParkingService.reportParkingInRecord(param);
    }

    /**
     * 上报车场出场记录
     *
     * @return
     */
    @PostMapping("/public/aike/parking2-out-record/{tenant}/reportCarOut")
    public AiKe2Response reportParking2OutRecord(@PathVariable Integer tenant,
                                                 @RequestBody String reportData,
                                                 HttpServletRequest request
                                                ) {
        String ipAddr = request.getRemoteAddr();
        log.info("艾科停车场，离场调用者IP地址：{}", ipAddr);
        log.info("艾科停车场，离场上报：{}", reportData);
        JSONObject inRecordJson = JSONObject.parseObject(reportData).getJSONObject("biz_content");
        String carOutRecord = inRecordJson.toJSONString();
        try {
            carOutRecord = URLDecoder.decode(carOutRecord, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("艾科停车场，离场上报,解码异常", e);
        }
        AiKeParking2OutRecordReportParam param = JsonUtils.deserialize(carOutRecord, AiKeParking2OutRecordReportParam.class);
        param.setTenant(tenant);
        param.setOutUrl("http://" + ipAddr + ":8092" + param.getOutUrl());
        return this.aiKeParkingService.reportParkingOutRecord(param);
    }

    /**
     * 上报车场缴费记录
     *
     * @return
     */
    @PostMapping("/public/aike/parking2-out-record/{tenant}/reportParkingFee")
    public AiKe2Response reportParking2reportParkingFee(@PathVariable Integer tenant,
                                                        @RequestBody String reportData,
                                                        HttpServletRequest request
                                                       ) {
        String ipAddr = request.getRemoteAddr();
        log.info("艾科停车场，缴费记录调用者IP地址：{}", ipAddr);
        log.info("艾科停车场，缴费记录上报：{}", reportData);
        JSONObject inRecordJson = JSONObject.parseObject(reportData).getJSONObject("biz_content");
        String carOutRecord = inRecordJson.toJSONString();
        try {
            carOutRecord = URLDecoder.decode(carOutRecord, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("艾科停车场，缴费记录上报,解码异常", e);
        }
        AiKeParking2FeeRecordReportParam param = JsonUtils.deserialize(carOutRecord, AiKeParking2FeeRecordReportParam.class);
        param.setTenant(tenant);
        return this.aiKeParkingService.reportParkingPayRecord(param);
    }

    /**
     * 上报车场缴费记录
     *
     * @return
     */
    @PostMapping("/public/aike/parking6-out-record/{tenant}/reportParkingFee")
    public AiKe2Response reportParking6reportParkingFee(@PathVariable Integer tenant,
                                                        @RequestBody String reportData,
                                                        HttpServletRequest request
                                                       ) {
        String ipAddr = request.getRemoteAddr();
        log.info("艾科停车场v6，缴费记录调用者IP地址：{}", ipAddr);
        log.info("艾科停车场v6，缴费记录上报：{}", reportData);
        JSONObject inRecordJson = JSONObject.parseObject(reportData).getJSONObject("biz_content");
        String carOutRecord = inRecordJson.toJSONString();
        try {
            carOutRecord = URLDecoder.decode(carOutRecord, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("艾科停车场，缴费记录上报,解码异常", e);
        }
        AiKeParkingV6FeeRecordReportParam param = JsonUtils.deserialize(carOutRecord, AiKeParkingV6FeeRecordReportParam.class);
        param.setTenant(tenant);
        return this.aiKeParkingService.reportParkingPayRecordV6(param);
    }
}
