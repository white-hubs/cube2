package com.guhecloud.cube.biz.adapter.park.server.application.jieshun.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.jieshun.model.JieShunResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.jieshun.param.JieShunParkingInRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.jieshun.param.JieShunParkingOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.jieshun.param.JieShunParkingPlaceReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.jieshun.service.JieShunParkingService;
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
 * JieLink 3.*版本 停车场接口
 *
 * @author appet
 * @date 2023/10/11 16:45
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class JieShunParkingController {
    private final JieShunParkingService jieShunParkingService;

    /**
     * 获取车场信息
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/js/parking-info")
    public List<ParkingInfoVO> getIntercityParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        return this.jieShunParkingService.getParkingInfo();
    }

    /**
     * 车场数据订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/js/parking-subscribe")
    public Boolean subscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.jieShunParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/js/parking-un-subscribe")
    public Boolean unSubscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.jieShunParkingService.parkingUnSubscribe(param);
    }

    /**
     * 上报车场入场记录
     *
     * @return
     */
    @PostMapping("/public/js/{tenant}/carin")
    public JieShunResponse reportParkingInRecord(@PathVariable Integer tenant,
                                                 @Valid @RequestBody JieShunParkingInRecordReportParam param
                                                ) {
        log.info("js停车场，上报车场入场记录：{}", param.toString());
        param.setTenant(tenant);
        return this.jieShunParkingService.reportParkingInRecord(param);
    }

    /**
     * 上报车场出场记录
     *
     * @return
     */
    @PostMapping("/public/js/{tenant}/carout")
    public JieShunResponse reportParkingOutRecord(@PathVariable Integer tenant,
                                                  @Valid @RequestBody JieShunParkingOutRecordReportParam param
                                                 ) {
        log.info("js停车场，上报车场出场记录：{}", param.toString());
        param.setTenant(tenant);
        this.jieShunParkingService.reportParkingOutRecord(param);
        return this.jieShunParkingService.reportParkingPayRecord(param);
    }

    /**
     * 车位数上报
     *
     * @param tenant
     * @param param
     *
     * @return
     */
    @PostMapping("/public/js/{tenant}/parktotalplace")
    public JieShunResponse reportParkingPlace(@PathVariable Integer tenant,
                                              @Valid @RequestBody JieShunParkingPlaceReportParam param
                                             ) {
        log.info("jieshun停车场，车位数上报：{}", param.toString());
        param.setTenant(tenant);
        return this.jieShunParkingService.reportParkingPlace(param);
    }

}
