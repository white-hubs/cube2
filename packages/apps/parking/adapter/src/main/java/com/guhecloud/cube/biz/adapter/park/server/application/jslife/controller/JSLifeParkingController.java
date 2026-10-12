package com.guhecloud.cube.biz.adapter.park.server.application.jslife.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.biz.adapter.park.server.application.jslife.request.JSLifeParkInPushQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jslife.request.JSLifeParkInfoPushQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jslife.request.JSLifeParkOutPushQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jslife.service.JSLifeParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.jslife.vo.JSLifeResponse;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
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

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JSLifeParkingController {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final JSLifeParkingService JSLifeParkingService;

    /**
     * 获取车场信息
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/jieshun/parking-info")
    public List<ParkingInfoVO> getParkingInfo() {
        return this.JSLifeParkingService.getParkingInfo();
    }

    /**
     * 车场数据订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/jieshun/parking-subscribe")
    public Boolean subscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.JSLifeParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/jieshun/parking-un-subscribe")
    public Boolean unSubscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.JSLifeParkingService.parkingUnSubscribe(param);
    }

    /**
     * 上报车场数
     *
     * @return
     */
    @PostMapping("/public/jieshun/{tenant}/park/parkSpace")
    public JSLifeResponse reportParkingInfoRecord(@PathVariable Integer tenant, @Valid @RequestBody JSLifeParkInfoPushQo param,
                                                  HttpServletRequest request
                                                 ) throws IOException {
        String ipAddr = request.getRemoteAddr();
        log.info("捷顺停车场，停车场调用者IP地址:{}", ipAddr);
        log.info("捷顺停车场，车场上报：{}", param);

        if (CollectionUtils.isEmpty(param.getDataItems())) {
            throw new ServiceException("捷顺停车场，车场上报数据为空");
        }
        return this.JSLifeParkingService.reportParkingInfoRecordService(param.getDataItems(), tenant);
    }

    /**
     * 上报车场入场记录
     *
     * @return
     */
    @PostMapping("/public/jieshun/{tenant}/park/parkIn")
    public JSLifeResponse reportParkingInRecord(@PathVariable Integer tenant, @Valid @RequestBody JSLifeParkInPushQo param,
                                                HttpServletRequest request
                                               ) throws IOException {
        String ipAddr = request.getRemoteAddr();
        log.info("捷顺停车场，进场调用者IP地址:{}", ipAddr);
        log.info("捷顺停车场，进场上报：{}", param);
        if (CollectionUtils.isEmpty(param.getDataItems())) {
            throw new ServiceException("捷顺停车场，进场上报数据为空");
        }
        return this.JSLifeParkingService.reportParkingInRecordService(param.getDataItems(), tenant);
    }

    /**
     * 上报车场出场记录 && 上报车场缴费记录
     *
     * @return
     */
    @PostMapping("/public/jieshun/{tenant}/park/parkOut")
    public JSLifeResponse reportParkingOutRecord(@PathVariable Integer tenant, @Valid @RequestBody JSLifeParkOutPushQo param,
                                                 HttpServletRequest request
                                                ) throws IOException {
        String ipAddr = request.getRemoteAddr();
        log.info("捷顺停车场，出场&&缴费调用者IP地址:{}", ipAddr);
        log.info("捷顺停车场，出场&&缴费上报：{}", param);

        if (CollectionUtils.isEmpty(param.getDataItems())) {
            throw new ServiceException("捷顺停车场，出场&&缴费上报数据为空");
        }
        this.JSLifeParkingService.reportParkingOutRecord(param.getDataItems(), tenant);
        return this.JSLifeParkingService.reportParkingPayRecord(param.getDataItems(), tenant);
    }
}
