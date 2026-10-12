package com.guhecloud.cube.biz.adapter.park.server.application.bosnia.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.bosnia.param.ParkingInRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.bosnia.param.ParkingOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.bosnia.service.BosniaParkingService;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.BasicResponse;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayTotalQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayTotalVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 博西尼停车场接口
 *
 * @author lanchun
 * @date 2022/10/8 16:45
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingController {

    private final BosniaParkingService bosniaParkingService;

    /**
     * 获取车场信息
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/bosnia/parking-info")
    public List<ParkingInfoVO> getParkingInfo() {
        return this.bosniaParkingService.getParkingInfo();
    }

    /**
     * 获取车场缴费统计
     *
     * @param param
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/bosnia/parking-pay-total")
    public ParkingPayTotalVO getParkingPayTotal(ParkingPayTotalQueryParam param) throws Exception {
        return this.bosniaParkingService.getParkingPayTotal(param);
    }

    /**
     * 车场数据订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/bosnia/parking-subscribe")
    public Boolean subscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.bosniaParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/bosnia/parking-un-subscribe")
    public Boolean unSubscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.bosniaParkingService.parkingUnSubscribe(param);
    }

    /**
     * 上报车场入场记录
     *
     * @return
     */
    @PostMapping("/public/bosnia/report/parking-in-record/{tenant}")
    public BasicResponse reportParkingInRecord(@PathVariable("tenant") Integer tenant,
                                               @Valid @RequestBody ParkingInRecordReportParam param
                                              ) {
        param.setTenant(tenant);
        return this.bosniaParkingService.reportParkingInRecord(param);
    }

    /**
     * 上报车场出场记录
     *
     * @return
     */
    @PostMapping("/public/bosnia/report/parking-out-record/{tenant}")
    public BasicResponse reportParkingOutRecord(@PathVariable("tenant") Integer tenant,
                                                @Valid @RequestBody ParkingOutRecordReportParam param
                                               ) {
        param.setTenant(tenant);
        this.bosniaParkingService.reportParkingOutRecord(param);
        return this.bosniaParkingService.reportParkingFeeRecord(param);
    }
}
