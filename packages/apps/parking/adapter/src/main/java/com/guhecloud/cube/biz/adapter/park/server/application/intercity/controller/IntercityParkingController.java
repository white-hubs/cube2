package com.guhecloud.cube.biz.adapter.park.server.application.intercity.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.intercity.param.IntercityParkingInRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.intercity.param.IntercityParkingOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.intercity.service.IntercityParkingService;
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

/**
 * 城际停车场接口
 *
 * @author lanchun
 * @date 2022/10/8 16:45
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IntercityParkingController {
    private final IntercityParkingService intercityParkingService;

    /**
     * 获取车场信息
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/intercity/parking-info")
    public List<ParkingInfoVO> getIntercityParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        return this.intercityParkingService.getParkingInfo();
    }

    /**
     * 车场数据订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/intercity/parking-subscribe")
    public Boolean subscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.intercityParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/intercity/parking-un-subscribe")
    public Boolean unSubscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.intercityParkingService.parkingUnSubscribe(param);
    }

    /**
     * 上报车场入场记录
     *
     * @return
     */
    @PostMapping("/public/intercity/report/parking-in-record/{tenant}")
    public BasicResponse reportParkingInRecord(@PathVariable Integer tenant,
                                               @Valid @RequestBody IntercityParkingInRecordReportParam param
                                              ) {
        param.setTenant(tenant);
        log.info("城际上报车场入场记录====={}", param);
        return this.intercityParkingService.reportParkingInRecord(param);
    }

    /**
     * 上报车场出场记录
     *
     * @return
     */
    @PostMapping("/public/intercity/report/parking-out-record/{tenant}")
    public BasicResponse reportParkingOutRecord(@PathVariable Integer tenant,
                                                @Valid @RequestBody IntercityParkingOutRecordReportParam param
                                               ) {
        param.setTenant(tenant);
        log.info("城际上报车场出场记录====={}", param);
        return this.intercityParkingService.reportParkingOutRecord(param);
    }

}
