package com.guhecloud.cube.biz.adapter.park.server.application.ketuo.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.ketuo.param.KeTuoParkingInRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.ketuo.param.KeTuoParkingOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.ketuo.service.KeTuoParkingService;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.BasicResponse;
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

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeTuoController {

    private final KeTuoParkingService keTuoParkingService;

    /**
     * 获取车场信息
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/ketuo/parking-info")
    public List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        return this.keTuoParkingService.getParkingInfo();
    }

    /**
     * 车场数据订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/ketuo/parking-subscribe")
    public Boolean subscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.keTuoParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/ketuo/parking-un-subscribe")
    public Boolean unSubscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.keTuoParkingService.parkingUnSubscribe(param);
    }

    /**
     * 上报车场入场记录
     *
     * @return
     */
    @PostMapping("/public/ketuo/{tenant}/PostCarInInfo")
    public BasicResponse reportParkingInRecord(@PathVariable Integer tenant, @Valid @RequestBody KeTuoParkingInRecordReportParam param) {
        param.setTenant(tenant);
        log.info("科拓上报车场入场记录====={}", param);
        String[] split = param.getPassName().split("-");
        param.setPassName(split[0]);
        param.setPassNo(split[1]);
        return this.keTuoParkingService.reportParkingInRecord(param);
    }

    /**
     * 上报车场出场记录
     *
     * @return
     */
    @PostMapping("/public/ketuo/{tenant}/PostCarOutInfo")
    public BasicResponse reportParkingOutRecord(@PathVariable Integer tenant, @Valid @RequestBody KeTuoParkingOutRecordReportParam param) {
        param.setTenant(tenant);
        log.info("科拓上报车场出场记录====={}", param);
        String[] split = param.getPassName().split("-");
        param.setPassName(split[0]);
        param.setPassNo(split[1]);
        param.setOutImg(param.getOutUrl());
        return this.keTuoParkingService.reportParkingOutRecord(param);
    }
}
