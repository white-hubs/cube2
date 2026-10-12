package com.guhecloud.cube.biz.adapter.park.server.application.hikvision.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.hikvision.service.HikParkingService;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
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
import java.util.List;

/**
 * @author wql
 * @describe 海康威视停车场对接
 * @date 2024/3/5 11:49
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HikParkingController {
    private final HikParkingService hikParkingService;

    /**
     * 获取停车场信息
     *
     * @return 停车场信息
     */
    @GetMapping("/hik/parking-info")
    public List<ParkingInfoVO> getIntercityParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        return this.hikParkingService.getParkingInfo();
    }

    /**
     * 按事件类型订阅事件
     */
    @GetMapping("/hik/eventSubscribe")
    public void hikEventSubscribe() {
        this.hikParkingService.hikEventSubscribe();
    }

    /**
     * 车场数据订阅
     *
     * @param param 停车场订阅参数
     *
     * @return 操作结果
     */
    @PostMapping("/hik/parking-subscribe")
    public Boolean subscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.hikParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     *
     * @param param 停车场订阅参数
     *
     * @return 操作结果
     */
    @PostMapping("/hik/parking-un-subscribe")
    public Boolean unSubscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.hikParkingService.parkingUnSubscribe(param);
    }

    /**
     * 上报车场出入场记录
     *
     * @return 上报结果
     */
    @PostMapping("/public/report/{tenant}/ParkingOutInRecord")
    public String reportParkingOutInRecord(@PathVariable Integer tenant, @RequestBody String param) {
        log.info("海康上报车场出入场记录,tenant={},param={}", tenant, param);
        this.hikParkingService.reportParkingOutInRecord(tenant, param);
        return "ok";
    }

    /**
     * 测试事件上报订阅
     *
     * @param param 订阅参数
     *
     * @return 返回信息
     */
    @PostMapping("/public/api/eventService/v1/eventSubscriptionByEventTypes")
    public String hikEventReportMock(@RequestBody String param) {
        log.info("事件上报参数:{}", param);
        return "ok";
    }

    /**
     * 测试停车场信息采集
     *
     * @param param 测试请求参数
     *
     * @return 测试采集信息
     */
    @PostMapping("/public/api/pms/v1/park/remainSpaceNum")
    public String hikParkInfoGatherMock(@RequestBody String param) {
        log.info("事件上报参数:{}", param);
        return "{\n" +
               "    \"code\": \"0\",\n" +
               "    \"msg\": \"success\",\n" +
               "    \"data\": [\n" +
               "        {\n" +
               "            \"parkSyscode\": \"h45h3gh3g34g324g345\",\n" +
               "            \"parkName\": \"停车库1\",\n" +
               "            \"parentParkSyscode\": \"g354g45g345g35g2345gh5\",\n" +
               "            \"totalPlace\": 5000,\n" +
               "            \"totalPermPlace\": 5000,\n" +
               "            \"totalReservePlace\": 0,\n" +
               "            \"leftPlace\": 0,\n" +
               "            \"leftPermPlace\": 0,\n" +
               "            \"leftReservePlace\": 0\n" +
               "        }\n" +
               "    ]\n" +
               "}";
    }

}
